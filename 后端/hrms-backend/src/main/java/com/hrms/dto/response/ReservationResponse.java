package com.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 预订接口响应对象，字段兼容前端预订列表。
 */
public class ReservationResponse {
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
    public String statusName;
    public String remark;
}

