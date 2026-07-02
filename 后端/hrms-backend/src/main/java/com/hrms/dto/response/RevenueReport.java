package com.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 营收报表响应对象。
 */
public record RevenueReport(LocalDate date, BigDecimal totalRevenue, BigDecimal roomRevenue,
                            BigDecimal serviceRevenue, BigDecimal paidAmount, BigDecimal unpaidAmount,
                            int billCount) {
}
