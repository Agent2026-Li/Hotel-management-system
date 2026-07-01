package com.hrms.service;

import com.hrms.dto.response.OccupancyReport;
import com.hrms.dto.response.RevenueReport;

import java.time.LocalDate;

/**
 * 报表统计业务接口。
 */
public interface ReportService {
    OccupancyReport occupancy(LocalDate date);

    RevenueReport revenue(LocalDate date);
}
