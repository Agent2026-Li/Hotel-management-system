package com.hrms.utils;

import com.hrms.entity.BillItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * 金额计算工具，负责账单房费、服务费和余额计算。
 */
public final class AmountUtils {
    private AmountUtils() {
    }

    public static BigDecimal roomAmount(List<BillItem> items) {
        return items.stream()
                .filter(item -> "ROOM".equals(item.category))
                .map(BillItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal serviceAmount(List<BillItem> items) {
        return items.stream()
                .filter(item -> !"ROOM".equals(item.category))
                .map(BillItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static String billStatus(BigDecimal totalAmount, BigDecimal paidAmount) {
        BigDecimal paid = paidAmount == null ? BigDecimal.ZERO : paidAmount;
        return paid.compareTo(totalAmount) >= 0 ? "paid" : "unpaid";
    }
}
