# 04 数据库设计

数据库：`hrms`，字符集 `utf8mb4`。

## 核心表

- `sys_role`: 角色。
- `sys_user`: 测试账号。
- `sys_user_role`: 用户角色关系。
- `room_type`: 房型，价格使用 `DECIMAL(10,2)`。
- `room`: 房间，98 间，状态使用 `VC/OC/RS/VD/CLEANING/OOO`。
- `guest`: 客史。
- `reservation`: 预订。
- `check_in_record`: 入住记录。
- `bill`: 账单主表。
- `bill_item`: 账单明细。
- `housekeeping_task`: 清洁任务。

## 索引

- 房间：`floor/status`。
- 预订：`status/checkin/checkout`。
- 入住：`room_number/status`。
- 账单：`room_number/status`。
- 清洁：`room_number/status`。

项目 SQL 只包含 `src/main/resources/db/schema.sql` 和 `src/main/resources/db/seed.sql`，数据库工具导出的其它 schema 目录不属于后端工程。

