package com.hrms.service;

import com.hrms.dto.request.ChargeRequest;
import com.hrms.dto.response.BillResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账单业务接口。
 */
public interface BillingService {
    List<BillResponse> list(String status, String roomNumber);

    BillResponse get(String id);

    BillResponse charge(String id, ChargeRequest request);

    BillResponse pay(String id, BigDecimal paidAmount);
}

