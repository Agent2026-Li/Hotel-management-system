package com.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 客史响应对象，字段兼容前端客户档案页面。
 */
public class GuestResponse {
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

