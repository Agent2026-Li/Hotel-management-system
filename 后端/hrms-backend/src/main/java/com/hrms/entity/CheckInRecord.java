package com.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入住记录实体，对应 check_in_record 表。
 */
public class CheckInRecord {
    public String id;
    public String reservationId;
    public String guestId;
    public String roomNumber;
    public LocalDateTime checkinTime;
    public LocalDate expectedCheckout;
    public LocalDateTime checkoutTime;
    public String status;
    public String billId;
}
