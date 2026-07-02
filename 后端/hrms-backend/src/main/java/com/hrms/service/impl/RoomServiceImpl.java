package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.dto.request.ChangeRoomRequest;
import com.hrms.dto.request.ExtendStayRequest;
import com.hrms.dto.request.RoomStatusUpdateRequest;
import com.hrms.dto.response.RoomResponse;
import com.hrms.entity.Room;
import com.hrms.mapper.BillMapper;
import com.hrms.mapper.CheckInRecordMapper;
import com.hrms.mapper.GuestMapper;
import com.hrms.mapper.RoomMapper;
import com.hrms.service.RoomService;
import com.hrms.utils.RoomStatusWebSocketHandler;
import com.hrms.utils.StatusMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 房态业务实现，负责房间查询、房态修改、换房和续住。
 */
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomMapper roomMapper;
    private final CheckInRecordMapper checkInRecordMapper;
    private final BillMapper billMapper;
    private final GuestMapper guestMapper;
    private final RoomStatusWebSocketHandler webSocketHandler;

    public RoomServiceImpl(RoomMapper roomMapper, CheckInRecordMapper checkInRecordMapper, BillMapper billMapper,
                           GuestMapper guestMapper, RoomStatusWebSocketHandler webSocketHandler) {
        this.roomMapper = roomMapper;
        this.checkInRecordMapper = checkInRecordMapper;
        this.billMapper = billMapper;
        this.guestMapper = guestMapper;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public List<RoomResponse> list(String status, Integer floor) {
        String internalStatus = status == null || status.isBlank() ? null : StatusMapper.toInternal(status);
        return roomMapper.selectRoomList(internalStatus, floor);
    }

    @Override
    public RoomResponse get(String number) {
        RoomResponse room = roomMapper.selectRoomResponseByNumber(number);
        if (room == null) {
            throw new BusinessException(404, "房间不存在");
        }
        return room;
    }

    @Override
    @Transactional
    public RoomResponse updateStatus(String number, RoomStatusUpdateRequest request) {
        ensureRoom(number);
        String internalStatus = StatusMapper.toInternal(request.status);
        roomMapper.updateStatus(number, internalStatus);
        RoomResponse room = get(number);
        webSocketHandler.broadcastRoom(room);
        return room;
    }

    @Override
    @Transactional
    public RoomResponse changeRoom(String number, ChangeRoomRequest request) {
        Room source = ensureRoom(number);
        Room target = ensureRoom(request.targetRoomNumber);
        if (!"OC".equals(source.status)) {
            throw new BusinessException(400, "只有在住房可以办理换房");
        }
        if (!"VC".equals(target.status)) {
            throw new BusinessException(400, "目标房间必须为空闲房");
        }

        roomMapper.updateStatus(source.roomNumber, "VD");
        roomMapper.updateStatus(target.roomNumber, "OC");
        checkInRecordMapper.updateRoomNumber(source.roomNumber, target.roomNumber);
        billMapper.updateRoomNumber(source.roomNumber, target.roomNumber);
        guestMapper.updateRoomNumber(source.roomNumber, target.roomNumber);

        webSocketHandler.broadcastRoom(get(source.roomNumber));
        RoomResponse changed = get(target.roomNumber);
        webSocketHandler.broadcastRoom(changed);
        return changed;
    }

    @Override
    @Transactional
    public RoomResponse extendStay(String number, ExtendStayRequest request) {
        Room room = ensureRoom(number);
        if (!"OC".equals(room.status)) {
            throw new BusinessException(400, "只有在住房可以办理续住");
        }
        LocalDate checkout = LocalDate.parse(request.checkout);
        checkInRecordMapper.updateExpectedCheckout(number, checkout);
        guestMapper.updateCheckout(number, checkout);
        return get(number);
    }

    private Room ensureRoom(String number) {
        Room room = roomMapper.getByRoomNumber(number);
        if (room == null) {
            throw new BusinessException(404, "房间不存在");
        }
        return room;
    }
}

