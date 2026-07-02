package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求参数。
 */
public class LoginRequest {
    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
