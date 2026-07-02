package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.dto.request.LoginRequest;
import com.hrms.dto.response.LoginResponse;
import com.hrms.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 登录认证接口控制器，负责登录和测试账号查询。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录，返回 Token 和用户角色。
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    /**
     * 查询测试账号，方便前端登录页展示。
     */
    @Operation(summary = "查询测试账号")
    @GetMapping("/test-accounts")
    public ApiResponse<List<Map<String, String>>> testAccounts() {
        return ApiResponse.success(authService.testAccounts());
    }
}

