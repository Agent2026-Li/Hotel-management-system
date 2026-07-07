# 06 开发任务清单

- Spring Boot 工程：已完成。
- 统一响应、异常、鉴权、RBAC：已完成。
- 房态、预订、入住、退房、账单、清洁、客史、报表 API：已完成。
- MySQL `schema.sql`、`seed.sql`：已完成。
- WebSocket `/ws/room-status`：已完成最小可运行实现。
- 单元测试：覆盖登录、房态映射、预订校验、入住、退房、账单、清洁流转。

验收标准：`mvn test` 通过；`http/hrms-api.http` 可按测试链路调用；`db` 目录只包含项目 SQL。

