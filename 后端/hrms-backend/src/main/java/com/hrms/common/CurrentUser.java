package com.hrms.common;

/**
 * 当前登录用户信息，由 Token 解析后放入请求上下文。
 */
public record CurrentUser(String username, String name, String role) {
}
