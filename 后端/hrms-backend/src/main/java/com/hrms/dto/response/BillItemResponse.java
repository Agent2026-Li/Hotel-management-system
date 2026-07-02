package com.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单明细响应对象。
 */
public class BillItemResponse {
    public String id;
    public String billId;
    public String itemName;
    public String category;
    public BigDecimal amount;
    public Integer quantity;
    public String remark;
    public LocalDateTime createdAt;
}

