# 02 后端初步设计

## 架构

单体 Spring Boot 工程：`com.hrms`。当前实现按 `common/config/controller/service/mapper/entity/dto/utils` 分层组织，业务能力覆盖认证、房态、预订、入住、退房、账单、清洁、客史和报表。

## 模块职责

- `common`: 统一响应、异常处理、鉴权拦截、角色校验和业务异常。
- `config`: Web MVC 拦截器和 WebSocket 配置。
- `controller`: 对外提供登录、房态、预订、入住、退房、账单、清洁、客史和报表接口。
- `service` / `service.impl`: 编排业务流程、校验状态流转、计算金额。
- `mapper`: 通过 MyBatis 访问 MySQL，简单 SQL 使用注解，复杂 SQL 使用 XML。
- `entity`: 映射房间、预订、入住记录、账单、清洁任务等核心表。
- `dto.request` / `dto.response`: 隔离接口入参、出参和数据库实体。
- `utils`: Token、房态映射、金额、ID 生成和 WebSocket 房态推送。

## 关键流程

1. 新增预订：校验日期、房型、空房，生成预订并将房间置为 `reserved`。
2. 入住：预订或 Walk-in 入住后，创建客史、入住记录、账单，房间置为 `occupied`。
3. 消费入账：账单增加 `BillItem`，用 `BigDecimal` 重算金额。
4. 退房：账单结算，入住记录置为已退房，房间置为 `dirty`，自动生成清洁任务。
5. 清洁：任务 `doing/completed/done` 流转，检查通过后房间置为 `vacant`。

