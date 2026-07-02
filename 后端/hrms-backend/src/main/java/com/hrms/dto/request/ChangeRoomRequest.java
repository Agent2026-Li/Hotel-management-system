package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 换房请求参数。
 */
public class ChangeRoomRequest {
    @NotBlank
    public String targetRoomNumber;
    public String reason;
}
