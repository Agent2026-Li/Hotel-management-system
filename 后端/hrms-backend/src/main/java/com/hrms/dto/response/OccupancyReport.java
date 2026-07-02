package com.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 入住率报表响应对象。
 */
public record OccupancyReport(LocalDate date, int total, int vacant, int occupied, int reserved, int dirty,
                              BigDecimal occupancyRate) {
}

