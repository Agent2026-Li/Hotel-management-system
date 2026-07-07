package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 清洁任务状态更新请求参数。
 */
public class HousekeepingUpdateRequest {
    @NotBlank
    public String status;
    public String assignedTo;
    public String remark;
}
