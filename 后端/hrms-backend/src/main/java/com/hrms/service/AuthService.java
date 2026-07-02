package com.hrms.service;

import com.hrms.dto.request.LoginRequest;
import com.hrms.dto.response.LoginResponse;

import java.util.List;
import java.util.Map;

/**
 * 登录认证业务接口。
 */
public interface AuthService {
    LoginResponse login(LoginRequest request);

    List<Map<String, String>> testAccounts();
}

