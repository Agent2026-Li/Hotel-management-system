package com.hrms.utils;

import com.hrms.common.BusinessException;

import java.util.Map;

/**
 * 房态转换工具，负责内部房态码和前端房态字段互转。
 */
public final class StatusMapper {
    private static final Map<String, String> FRONTEND_STATUS = Map.of(
            "VC", "vacant",
            "OC", "occupied",
            "RS", "reserved",
            "VD", "dirty",
            "CLEANING", "cleaning",
            "OOO", "maintenance"
    );

    private static final Map<String, String> STATUS_NAME = Map.of(
            "VC", "空净房",
            "OC", "入住",
            "RS", "预订",
            "VD", "脏房",
            "CLEANING", "清洁中",
            "OOO", "维修"
    );

    private StatusMapper() {
    }

    public static String toFrontend(String internalStatus) {
        return FRONTEND_STATUS.getOrDefault(normalizeInternal(internalStatus), "vacant");
    }

    public static String statusName(String internalStatus) {
        return STATUS_NAME.getOrDefault(normalizeInternal(internalStatus), "未知");
    }

    public static String reservationStatusName(String status) {
        return switch (status == null ? "" : status) {
            case "confirmed" -> "已确认";
            case "cancelled" -> "已取消";
            case "checked_in" -> "已入住";
            case "checked_out" -> "已退房";
            default -> "未知";
        };
    }

    public static String billStatusName(String status) {
        return "paid".equals(status) ? "已结清" : "未结清";
    }

    public static String housekeepingStatusName(String status) {
        return switch (status == null ? "" : status) {
            case "pending" -> "待清洁";
            case "doing" -> "清洁中";
            case "completed" -> "待检查";
            case "done", "inspected" -> "已完成";
            default -> "未知";
        };
    }

    public static String toInternal(String status) {
        if (status == null || status.isBlank()) {
            return "VC";
        }
        String normalized = status.trim().toUpperCase();
        return switch (normalized) {
            case "VACANT", "VC" -> "VC";
            case "OCCUPIED", "OC" -> "OC";
            case "RESERVED", "RS" -> "RS";
            case "DIRTY", "VD" -> "VD";
            case "CLEANING" -> "CLEANING";
            case "MAINTENANCE", "OOO" -> "OOO";
            default -> throw new BusinessException(400, "不支持的房态: " + status);
        };
    }

    public static String normalizeInternal(String status) {
        return status == null ? "VC" : status.trim().toUpperCase();
    }
}
