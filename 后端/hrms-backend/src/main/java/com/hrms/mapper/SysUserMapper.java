package com.hrms.mapper;

import com.hrms.dto.response.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统用户 Mapper，负责登录账号和角色查询。
 */
@Mapper
public interface SysUserMapper {
    /** 根据用户名查询可登录用户和角色。 */
    @Select("""
            select u.username, u.password, u.real_name as name, r.role_code as role
            from sys_user u
            join sys_user_role ur on u.id = ur.user_id
            join sys_role r on ur.role_id = r.id
            where u.username = #{username} and u.enabled = 1
            limit 1
            """)
    AuthUser findAuthUserByUsername(String username);

    /** 查询全部测试账号。 */
    @Select("""
            select u.username, u.password, u.real_name as name, r.role_code as role
            from sys_user u
            join sys_user_role ur on u.id = ur.user_id
            join sys_role r on ur.role_id = r.id
            where u.enabled = 1
            order by u.id
            """)
    List<AuthUser> findTestAccounts();
}

