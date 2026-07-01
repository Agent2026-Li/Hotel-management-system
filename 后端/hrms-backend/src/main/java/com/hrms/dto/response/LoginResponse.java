package com.hrms.dto.response;

/**
 * 登录响应数据，返回 Token 和当前用户身份信息。
 */
public record LoginResponse(String token, String tokenType, String username, String name, String role, long expiresInHours) {
}
