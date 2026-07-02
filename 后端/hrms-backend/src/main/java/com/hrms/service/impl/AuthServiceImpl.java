package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.dto.request.LoginRequest;
import com.hrms.dto.response.AuthUser;
import com.hrms.dto.response.LoginResponse;
import com.hrms.mapper.SysUserMapper;
import com.hrms.service.AuthService;
import com.hrms.utils.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 登录认证业务实现，基于数据库用户和角色完成认证。
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final SysUserMapper sysUserMapper;
    private final TokenService tokenService;

    public AuthServiceImpl(SysUserMapper sysUserMapper, TokenService tokenService) {
        this.sysUserMapper = sysUserMapper;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        AuthUser user = sysUserMapper.findAuthUserByUsername(request.username);
        if (user == null || !user.password.equals(request.password)) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        return new LoginResponse(tokenService.generate(user), "Bearer", user.username, user.name, user.role, tokenService.getValidHours());
    }

    @Override
    public List<Map<String, String>> testAccounts() {
        return sysUserMapper.findTestAccounts().stream()
                .map(user -> Map.of(
                        "username", user.username,
                        "password", user.password,
                        "name", user.name,
                        "role", user.role
                ))
                .toList();
    }
}

