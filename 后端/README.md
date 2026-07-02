# 酒店管理系统后端

本目录是后端交付物，服务于现有产品经理文档和前端页面联调。后端接口保持 `/api/...` 路径和前端字段兼容，不修改前端代码。

## 技术栈

- JDK 17+
- Spring Boot 3.3.1
- 原生 MyBatis 3.0.3
- MySQL 8.0
- Token + RBAC 角色校验
- Knife4j / Swagger

## 目录

- `hrms-backend`: Spring Boot 后端工程
- `hrms-backend/src/main/java/com/hrms/controller`: 接口入口
- `hrms-backend/src/main/java/com/hrms/service/impl`: 业务实现
- `hrms-backend/src/main/java/com/hrms/mapper`: MyBatis Mapper 接口
- `hrms-backend/src/main/resources/mapper`: 复杂 SQL XML
- `hrms-backend/src/main/resources/db/schema.sql`: 建表脚本
- `hrms-backend/src/main/resources/db/seed.sql`: 测试数据脚本
- `docs`: 设计、接口、部署、测试交接文档
- `http/hrms-api.http`: 测试人员可直接调用的接口样例

## 运行目标

后端已从内存数据重构为 MySQL 持久化实现。接口优先兼容前端 mock 字段：房间 `number/floor/type/typeName/price/status/statusName/guest`，预订 `id/name/phone/roomType/roomNumber/checkin/checkout/nights/amount/status/statusName/remark`。

Mapper 编写规则：简单 SQL 写在 `Mapper.java` 注解里，复杂查询、动态条件、多表联查和统计 SQL 写在 `resources/mapper/*.xml`。
