package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.dto.request.ReservationRequest;
import com.hrms.dto.response.ReservationResponse;
import com.hrms.entity.Reservation;
import com.hrms.entity.Room;
import com.hrms.entity.RoomType;
import com.hrms.mapper.ReservationMapper;
import com.hrms.mapper.RoomMapper;
import com.hrms.mapper.RoomTypeMapper;
import com.hrms.service.ReservationService;
import com.hrms.utils.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 预订业务实现，负责预订查询、创建和取消。
 */
@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final IdGenerator idGenerator;

    public ReservationServiceImpl(ReservationMapper reservationMapper, RoomMapper roomMapper,
                                  RoomTypeMapper roomTypeMapper, IdGenerator idGenerator) {
        this.reservationMapper = reservationMapper;
        this.roomMapper = roomMapper;
        this.roomTypeMapper = roomTypeMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public List<ReservationResponse> list(String status) {
        return reservationMapper.selectReservationList(status);
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        validateDates(request);
        RoomType roomType = getRoomType(request.roomType);
        Room room = chooseRoom(request);

        Reservation reservation = buildReservation(idGenerator.reservationId(), request, roomType,
                room.roomNumber, "confirmed");
        reservationMapper.insert(reservation);
        roomMapper.updateStatus(room.roomNumber, "RS");
        return getResponse(reservation.id);
    }

    @Override
    @Transactional
    public ReservationResponse update(String id, ReservationRequest request) {
        validateDates(request);
        Reservation existing = getEntity(id);
        if ("checked_in".equals(existing.status)) {
            throw new BusinessException(400, "已入住的预订不能编辑");
        }
        if ("checked_out".equals(existing.status)) {
            throw new BusinessException(400, "已退房的预订不能编辑");
        }

        RoomType roomType = getRoomType(request.roomType);
        String roomNumber = existing.roomNumber;
        if ("confirmed".equals(existing.status)) {
            Room room = chooseRoomForUpdate(existing, request);
            roomNumber = room.roomNumber;
            syncReservedRoom(existing.roomNumber, roomNumber);
        } else if (!request.roomType.equals(existing.roomType)) {
            roomNumber = null;
        }

        Reservation reservation = buildReservation(id, request, roomType, roomNumber, existing.status);
        if (reservationMapper.update(reservation) == 0) {
            throw new BusinessException(404, "预订不存在");
        }
        return getResponse(id);
    }

    @Override
    @Transactional
    public ReservationResponse cancel(String id) {
        Reservation reservation = getEntity(id);
        if ("checked_in".equals(reservation.status)) {
            throw new BusinessException(400, "已入住的预订不能取消");
        }
        reservationMapper.updateStatus(id, "cancelled");
        Room room = roomMapper.getByRoomNumber(reservation.roomNumber);
        if (room != null && "RS".equals(room.status)) {
            roomMapper.updateStatus(room.roomNumber, "VC");
        }
        return getResponse(id);
    }

    private Reservation getEntity(String id) {
        Reservation reservation = reservationMapper.getById(id);
        if (reservation == null) {
            throw new BusinessException(404, "预订不存在");
        }
        return reservation;
    }

    private ReservationResponse getResponse(String id) {
        return reservationMapper.selectReservationList(null).stream()
                .filter(item -> item.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException(404, "预订不存在"));
    }

    private void validateDates(ReservationRequest request) {
        if (!request.checkout.isAfter(request.checkin)) {
            throw new BusinessException(400, "离店日期必须晚于入住日期");
        }
    }

    private RoomType getRoomType(String roomTypeId) {
        RoomType roomType = roomTypeMapper.getById(roomTypeId);
        if (roomType == null) {
            throw new BusinessException(404, "房型不存在");
        }
        return roomType;
    }

    private Reservation buildReservation(String id, ReservationRequest request, RoomType roomType,
                                         String roomNumber, String status) {
        long nights = ChronoUnit.DAYS.between(request.checkin, request.checkout);
        Reservation reservation = new Reservation();
        reservation.id = id;
        reservation.name = request.name;
        reservation.phone = request.phone;
        reservation.roomType = request.roomType;
        reservation.roomNumber = roomNumber;
        reservation.checkin = request.checkin;
        reservation.checkout = request.checkout;
        reservation.nights = Math.toIntExact(nights);
        reservation.amount = roomType.price.multiply(BigDecimal.valueOf(nights));
        reservation.status = status;
        reservation.remark = request.remark;
        return reservation;
    }

    private Room chooseRoom(ReservationRequest request) {
        if (request.roomNumber != null && !request.roomNumber.isBlank()) {
            Room selected = roomMapper.getByRoomNumber(request.roomNumber);
            if (selected == null) {
                throw new BusinessException(404, "指定房间不存在");
            }
            if (!request.roomType.equals(selected.typeId)) {
                throw new BusinessException(400, "指定房间与房型不匹配");
            }
            if (!"VC".equals(selected.status)) {
                throw new BusinessException(400, "指定房间不是空净房");
            }
            return selected;
        }
        Room room = roomMapper.findFirstVacantByType(request.roomType);
        if (room == null) {
            throw new BusinessException(409, "当前房型无可预订空房");
        }
        return room;
    }

    private Room chooseRoomForUpdate(Reservation existing, ReservationRequest request) {
        if (request.roomNumber != null && !request.roomNumber.isBlank()) {
            Room selected = roomMapper.getByRoomNumber(request.roomNumber);
            if (selected == null) {
                throw new BusinessException(404, "指定房间不存在");
            }
            if (!request.roomType.equals(selected.typeId)) {
                throw new BusinessException(400, "指定房间与房型不匹配");
            }
            boolean sameRoom = request.roomNumber.equals(existing.roomNumber);
            if (!sameRoom && !"VC".equals(selected.status)) {
                throw new BusinessException(400, "指定房间不是空净房");
            }
            if (sameRoom && !"RS".equals(selected.status) && !"VC".equals(selected.status)) {
                throw new BusinessException(400, "当前预订房间状态不能编辑");
            }
            return selected;
        }

        if (existing.roomNumber != null && !existing.roomNumber.isBlank()) {
            Room current = roomMapper.getByRoomNumber(existing.roomNumber);
            if (current != null && request.roomType.equals(current.typeId)
                    && ("RS".equals(current.status) || "VC".equals(current.status))) {
                return current;
            }
        }

        Room room = roomMapper.findFirstVacantByType(request.roomType);
        if (room == null) {
            throw new BusinessException(409, "当前房型无可预订空房");
        }
        return room;
    }

    private void syncReservedRoom(String oldRoomNumber, String newRoomNumber) {
        if (oldRoomNumber != null && !oldRoomNumber.equals(newRoomNumber)) {
            Room oldRoom = roomMapper.getByRoomNumber(oldRoomNumber);
            if (oldRoom != null && "RS".equals(oldRoom.status)) {
                roomMapper.updateStatus(oldRoom.roomNumber, "VC");
            }
        }
        roomMapper.updateStatus(newRoomNumber, "RS");
    }
}
