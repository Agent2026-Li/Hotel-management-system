package com.hrms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 酒店管理系统后端启动类，负责启动 Spring Boot 并扫描 MyBatis Mapper。
 */
@MapperScan("com.hrms.mapper")
@SpringBootApplication
public class HrmsBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmsBackendApplication.class, args);
    }
}
