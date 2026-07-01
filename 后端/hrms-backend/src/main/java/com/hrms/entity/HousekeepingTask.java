package com.hrms.entity;

import java.time.LocalDateTime;

/**
 * 清洁任务实体，对应 housekeeping_task 表。
 */
public class HousekeepingTask {
    public String id;
    public String roomNumber;
    public String taskType;
    public String status;
    public String assignedTo;
    public String priority;
    public LocalDateTime createdAt;
    public LocalDateTime completedAt;
    public String remark;
}
