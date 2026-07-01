package com.hrms.dto.response;

import java.time.LocalDateTime;

/**
 * 清洁任务响应对象，字段兼容前端清洁任务列表。
 */
public class HousekeepingTaskResponse {
    public String id;
    public String roomNumber;
    public String taskType;
    public String status;
    public String statusName;
    public String assignedTo;
    public String priority;
    public LocalDateTime createdAt;
    public LocalDateTime completedAt;
    public String remark;
}

