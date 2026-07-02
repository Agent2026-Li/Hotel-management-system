package com.hrms.dto.request;

import java.math.BigDecimal;

/**
 * 退房结算请求参数。
 */
public class CheckOutRequest {
    public String checkInId;
    public String roomNumber;
    public String paymentMethod;
    public BigDecimal paidAmount;
}
