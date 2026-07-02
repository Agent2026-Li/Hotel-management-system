package com.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单明细实体，对应 bill_item 表。
 */
public class BillItem {
    public String id;
    public String billId;
    public String itemName;
    public String category;
    public BigDecimal amount;
    public Integer quantity;
    public String remark;
    public LocalDateTime createdAt;

    public BigDecimal total() {
        return amount.multiply(BigDecimal.valueOf(quantity == null ? 1 : quantity));
    }
}
