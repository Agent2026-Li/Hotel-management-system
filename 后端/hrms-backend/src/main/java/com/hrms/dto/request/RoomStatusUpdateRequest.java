package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 房态修改请求参数。
 */
public class RoomStatusUpdateRequest {
    @NotBlank
    public String status;
    public String reason;
}
