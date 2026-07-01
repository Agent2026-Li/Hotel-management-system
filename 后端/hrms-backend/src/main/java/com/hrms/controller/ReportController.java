package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.dto.response.OccupancyReport;
import com.hrms.dto.response.RevenueReport;
import com.hrms.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 报表统计接口控制器，负责入住率和营收报表查询。
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 查询入住率报表。
     */
    @Operation(summary = "查询入住率报表")
    @GetMapping("/occupancy")
    public ApiResponse<OccupancyReport> occupancy(@RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(reportService.occupancy(date));
    }

    /**
     * 查询营收报表。
     */
    @Operation(summary = "查询营收报表")
    @GetMapping("/revenue")
    public ApiResponse<RevenueReport> revenue(@RequestParam(required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(reportService.revenue(date));
    }
}
