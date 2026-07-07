# 02 后端初步设计

## 架构

单体 Spring Boot 工程：`com.hrms`。模块按 `common/system/room/reservation/checkin/billing/housekeeping/guest/report` 拆分。

## 模块职责

- `common`: 统一响应、异常处理、Token、RBAC、状态映射、演示数据仓库。
- `system`: 登录、测试账号。
- `room`: 房间、房型、房态查询和变更、WebSocket 推送。
- `reservation`: 预订创建、取消、预订状态维护。
- `checkin`: 预订入住、Walk-in 入住、退房。
- `billing`: 账单查询、消费入账、金额计算。
- `housekeeping`: 清洁任务、检查通过、房态释放。
- `guest`: 客史查询。
- `report`: 入住率和营收报表。

## 关键流程

1. 新增预订：校验日期、房型、空房，生成预订并将房间置为 `reserved`。
2. 入住：预订或 Walk-in 入住后，创建客史、入住记录、账单，房间置为 `occupied`。
3. 消费入账：账单增加 `BillItem`，用 `BigDecimal` 重算金额。
4. 退房：账单结算，入住记录置为已退房，房间置为 `dirty`，自动生成清洁任务。
5. 清洁：任务 `doing/completed/done` 流转，检查通过后房间置为 `vacant`。

