package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 续住请求参数。
 */
public class ExtendStayRequest {
    @NotBlank
    public String checkout;
    public String reason;
}
