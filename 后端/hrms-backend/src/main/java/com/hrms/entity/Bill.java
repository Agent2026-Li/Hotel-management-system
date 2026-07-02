package com.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单实体，对应 bill 表。
 */
public class Bill {
    public String id;
    public String roomNumber;
    public String guestName;
    public String status;
    public BigDecimal roomAmount;
    public BigDecimal serviceAmount;
    public BigDecimal totalAmount;
    public BigDecimal paidAmount;
    public LocalDateTime createdAt;
}
