package com.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 客史实体，对应 guest 表。
 */
public class Guest {
    public String id;
    public String name;
    public String phone;
    public String idCard;
    public String roomNumber;
    public LocalDate checkin;
    public LocalDate checkout;
    public Integer totalOrders;
    public BigDecimal totalAmount;
    public String memberLevel;
}
