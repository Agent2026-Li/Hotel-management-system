package com.hrms.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 入住办理请求参数，支持预订入住和 Walk-in 入住。
 */
public class CheckInRequest {
    public String reservationId;
    public String roomNumber;
    public String name;
    public String phone;
    public String idCard;
    public LocalDate checkoutDate;
    public BigDecimal deposit;
}
