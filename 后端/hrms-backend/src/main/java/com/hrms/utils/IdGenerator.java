package com.hrms.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 业务 ID 生成工具，用于生成预订、入住、账单等主键。
 */
@Component
public class IdGenerator {
    private static final DateTimeFormatter DATE = DateTimeFormatter.BASIC_ISO_DATE;

    public String reservationId() {
        return "R" + DATE.format(LocalDate.now()) + shortSuffix();
    }

    public String guestId() {
        return "G" + DATE.format(LocalDate.now()) + shortSuffix();
    }

    public String checkInId() {
        return "CI" + DATE.format(LocalDate.now()) + shortSuffix();
    }

    public String billId() {
        return "B" + DATE.format(LocalDate.now()) + shortSuffix();
    }

    public String billItemId() {
        return "BI" + DATE.format(LocalDate.now()) + shortSuffix();
    }

    public String housekeepingId() {
        return "HK" + DATE.format(LocalDate.now()) + shortSuffix();
    }

    private String shortSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
