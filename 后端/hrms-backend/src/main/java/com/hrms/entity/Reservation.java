package com.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预订实体，对应 reservation 表。
 */
public class Reservation {
    public String id;
    public String name;
    public String phone;
    public String roomType;
    public String roomNumber;
    public LocalDate checkin;
    public LocalDate checkout;
    public Integer nights;
    public BigDecimal amount;
    public String status;
    public String remark;
    public LocalDateTime createdAt;
}
