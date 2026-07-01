package com.hrms.dto.response;

/**
 * 登录认证内部用户对象，由用户表和角色表联合查询得到。
 */
public class AuthUser {
    public String username;
    public String password;
    public String name;
    public String role;
}
