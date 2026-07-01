package com.hrms.service.impl;

import com.hrms.dto.response.OccupancyReport;
import com.hrms.dto.response.RevenueReport;
import com.hrms.mapper.ReportMapper;
import com.hrms.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * 报表业务实现，负责入住率和营收统计。
 */
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public OccupancyReport occupancy(LocalDate date) {
        int total = reportMapper.countRooms();
        int vacant = reportMapper.countRoomsByStatus("VC");
        int occupied = reportMapper.countRoomsByStatus("OC");
        int reserved = reportMapper.countRoomsByStatus("RS");
        int dirty = reportMapper.countRoomsByStatus("VD");
        BigDecimal rate = total == 0 ? BigDecimal.ZERO
                : BigDecimal.valueOf(occupied).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        return new OccupancyReport(date == null ? LocalDate.now() : date, total, vacant, occupied, reserved, dirty, rate);
    }

    @Override
    public RevenueReport revenue(LocalDate date) {
        BigDecimal roomRevenue = reportMapper.sumRoomRevenue();
        BigDecimal serviceRevenue = reportMapper.sumServiceRevenue();
        BigDecimal paidAmount = reportMapper.sumPaidAmount();
        BigDecimal total = roomRevenue.add(serviceRevenue);
        BigDecimal unpaid = total.subtract(paidAmount);
        return new RevenueReport(date == null ? LocalDate.now() : date, total, roomRevenue, serviceRevenue,
                paidAmount, unpaid, reportMapper.countBills());
    }
}
