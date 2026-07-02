package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入住记录响应对象。
 */
public class CheckInRecordResponse {
    public String id;
    public String reservationId;
    public String guestId;
    public String roomNumber;
    public String guestName;
    public String phone;
    public LocalDateTime checkinTime;
    public LocalDate expectedCheckout;
    public LocalDateTime checkoutTime;
    public String status;
    public String billId;
}

