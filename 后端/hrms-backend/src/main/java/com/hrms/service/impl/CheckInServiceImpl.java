package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.dto.request.CheckInRequest;
import com.hrms.dto.request.CheckOutRequest;
import com.hrms.dto.response.BillResponse;
import com.hrms.dto.response.CheckInRecordResponse;
import com.hrms.dto.response.CheckInResult;
import com.hrms.dto.response.CheckOutResult;
import com.hrms.dto.response.HousekeepingTaskResponse;
import com.hrms.entity.Bill;
import com.hrms.entity.BillItem;
import com.hrms.entity.CheckInRecord;
import com.hrms.entity.Guest;
import com.hrms.entity.Reservation;
import com.hrms.entity.Room;
import com.hrms.entity.RoomType;
import com.hrms.mapper.BillItemMapper;
import com.hrms.mapper.BillMapper;
import com.hrms.mapper.CheckInRecordMapper;
import com.hrms.mapper.GuestMapper;
import com.hrms.mapper.ReservationMapper;
import com.hrms.mapper.RoomMapper;
import com.hrms.mapper.RoomTypeMapper;
import com.hrms.service.BillingService;
import com.hrms.service.CheckInService;
import com.hrms.service.HousekeepingService;
import com.hrms.utils.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 入住和退房业务实现，负责 Walk-in、预订入住和退房结算。
 */
@Service
public class CheckInServiceImpl implements CheckInService {
    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final GuestMapper guestMapper;
    private final BillMapper billMapper;
    private final BillItemMapper billItemMapper;
    private final CheckInRecordMapper checkInRecordMapper;
    private final BillingService billingService;
    private final HousekeepingService housekeepingService;
    private final IdGenerator idGenerator;

    public CheckInServiceImpl(ReservationMapper reservationMapper, RoomMapper roomMapper, RoomTypeMapper roomTypeMapper,
                              GuestMapper guestMapper, BillMapper billMapper, BillItemMapper billItemMapper,
                              CheckInRecordMapper checkInRecordMapper, BillingService billingService,
                              HousekeepingService housekeepingService, IdGenerator idGenerator) {
        this.reservationMapper = reservationMapper;
        this.roomMapper = roomMapper;
        this.roomTypeMapper = roomTypeMapper;
        this.guestMapper = guestMapper;
        this.billMapper = billMapper;
        this.billItemMapper = billItemMapper;
        this.checkInRecordMapper = checkInRecordMapper;
        this.billingService = billingService;
        this.housekeepingService = housekeepingService;
        this.idGenerator = idGenerator;
    }

    @Override
    @Transactional
    public CheckInResult checkIn(CheckInRequest request) {
        Reservation reservation = findReservation(request.reservationId);
        String roomNumber = firstNotBlank(request.roomNumber, reservation == null ? null : reservation.roomNumber);
        if (roomNumber == null) {
            throw new BusinessException(400, "入住必须指定房间");
        }
        Room room = roomMapper.getByRoomNumber(roomNumber);
        if (room == null) {
            throw new BusinessException(404, "房间不存在");
        }
        if (!"VC".equals(room.status) && !"RS".equals(room.status)) {
            throw new BusinessException(400, "房间当前状态不能入住");
        }
        String guestName = firstNotBlank(request.name, reservation == null ? null : reservation.name);
        String phone = firstNotBlank(request.phone, reservation == null ? null : reservation.phone);
        if (guestName == null || phone == null) {
            throw new BusinessException(400, "入住客人姓名和手机号不能为空");
        }

        LocalDate checkoutDate = request.checkoutDate != null ? request.checkoutDate
                : reservation != null ? reservation.checkout : LocalDate.now().plusDays(1);
        if (!checkoutDate.isAfter(LocalDate.now().minusDays(1))) {
            throw new BusinessException(400, "预计离店日期不合法");
        }
        RoomType roomType = roomTypeMapper.getById(room.typeId);
        long nights = Math.max(1, ChronoUnit.DAYS.between(LocalDate.now(), checkoutDate));
        BigDecimal roomAmount = roomType.price.multiply(BigDecimal.valueOf(nights));

        Guest guest = new Guest();
        guest.id = idGenerator.guestId();
        guest.name = guestName;
        guest.phone = phone;
        guest.idCard = request.idCard == null ? "" : request.idCard;
        guest.roomNumber = room.roomNumber;
        guest.checkin = LocalDate.now();
        guest.checkout = checkoutDate;
        guest.totalOrders = 1;
        guest.totalAmount = roomAmount;
        guest.memberLevel = "普通会员";
        guestMapper.insert(guest);

        Bill bill = new Bill();
        bill.id = idGenerator.billId();
        bill.roomNumber = room.roomNumber;
        bill.guestName = guest.name;
        bill.status = "unpaid";
        bill.roomAmount = roomAmount;
        bill.serviceAmount = BigDecimal.ZERO;
        bill.totalAmount = roomAmount;
        bill.paidAmount = BigDecimal.ZERO;
        billMapper.insert(bill);

        BillItem item = new BillItem();
        item.id = idGenerator.billItemId();
        item.billId = bill.id;
        item.itemName = "房费";
        item.category = "ROOM";
        item.amount = roomAmount;
        item.quantity = 1;
        item.remark = "入住生成房费";
        billItemMapper.insert(item);

        CheckInRecord checkIn = new CheckInRecord();
        checkIn.id = idGenerator.checkInId();
        checkIn.reservationId = reservation == null ? null : reservation.id;
        checkIn.guestId = guest.id;
        checkIn.roomNumber = room.roomNumber;
        checkIn.checkinTime = LocalDateTime.now();
        checkIn.expectedCheckout = checkoutDate;
        checkIn.status = "living";
        checkIn.billId = bill.id;
        checkInRecordMapper.insert(checkIn);

        if (reservation != null) {
            reservationMapper.updateStatus(reservation.id, "checked_in");
        }
        roomMapper.updateStatus(room.roomNumber, "OC");
        return new CheckInResult(toRecordResponse(checkIn, guest), billingService.get(bill.id), roomMapper.selectRoomResponseByNumber(room.roomNumber));
    }

    @Override
    @Transactional
    public CheckOutResult checkOut(CheckOutRequest request) {
        CheckInRecord checkIn = findLivingCheckIn(request);
        BillResponse bill = billingService.pay(checkIn.billId, request.paidAmount);
        checkInRecordMapper.checkout(checkIn.id, LocalDateTime.now());
        roomMapper.updateStatus(checkIn.roomNumber, "VD");
        HousekeepingTaskResponse task = housekeepingService.createDirtyTask(checkIn.roomNumber, "退房自动生成清洁任务");
        CheckInRecord updated = checkInRecordMapper.getById(checkIn.id);
        Guest guest = guestMapper.getById(checkIn.guestId);
        return new CheckOutResult(toRecordResponse(updated, guest), bill, task);
    }

    private Reservation findReservation(String reservationId) {
        if (reservationId == null || reservationId.isBlank()) {
            return null;
        }
        Reservation reservation = reservationMapper.getById(reservationId);
        if (reservation == null) {
            throw new BusinessException(404, "预订不存在");
        }
        if (!"confirmed".equals(reservation.status)) {
            throw new BusinessException(400, "预订状态不能办理入住");
        }
        return reservation;
    }

    private CheckInRecord findLivingCheckIn(CheckOutRequest request) {
        if (request.checkInId != null && !request.checkInId.isBlank()) {
            CheckInRecord record = checkInRecordMapper.findLivingById(request.checkInId);
            if (record != null) {
                return record;
            }
        }
        if (request.roomNumber != null && !request.roomNumber.isBlank()) {
            CheckInRecord record = checkInRecordMapper.findLivingByRoomNumber(request.roomNumber);
            if (record != null) {
                return record;
            }
            throw new BusinessException(404, "该房间没有在住记录");
        }
        throw new BusinessException(400, "退房必须提供 checkInId 或 roomNumber");
    }

    private CheckInRecordResponse toRecordResponse(CheckInRecord record, Guest guest) {
        CheckInRecordResponse response = new CheckInRecordResponse();
        response.id = record.id;
        response.reservationId = record.reservationId;
        response.guestId = record.guestId;
        response.roomNumber = record.roomNumber;
        response.guestName = guest == null ? null : guest.name;
        response.phone = guest == null ? null : guest.phone;
        response.checkinTime = record.checkinTime;
        response.expectedCheckout = record.expectedCheckout;
        response.checkoutTime = record.checkoutTime;
        response.status = record.status;
        response.billId = record.billId;
        return response;
    }

    private String firstNotBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        if (second != null && !second.isBlank()) {
            return second;
        }
        return null;
    }
}
