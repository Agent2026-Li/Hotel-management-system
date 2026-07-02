package com.hrms.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 账单接口响应对象，包含账单金额和消费明细。
 */
public class BillResponse {
    public String id;
    public String roomNumber;
    public String guestName;
    public String status;
    public String statusName;
    public BigDecimal roomAmount;
    public BigDecimal serviceAmount;
    public BigDecimal totalAmount;
    public BigDecimal paidAmount;
    public BigDecimal balanceAmount;
    public List<BillItemResponse> items = new ArrayList<>();
}

