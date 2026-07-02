# 01 源文件阅读报告

## 后端需求

- 房态管理：查看房间、按楼层/状态筛选、修改房态。
- 预订管理：新增预订、取消预订、预订入住。
- 入住退房：Walk-in 入住、预订入住、退房结算、退房生成清洁任务。
- 账单管理：查询账单、消费入账、结算金额。
- 清洁管理：查看任务、更新清洁状态、检查通过后释放房间。
- 客史管理：按姓名/电话/证件查询客史。
- 报表：入住率、营收。
- 系统：登录、角色账号、基础权限。

## 前端字段证据

- 房间：`number`、`floor`、`type`、`typeName`、`price`、`status`、`statusName`、`guest`。
- 预订：`id`、`name`、`phone`、`roomType`、`checkin`、`checkout`、`nights`、`amount`、`status`、`statusName`。
- 清洁：`id`、`roomNumber`、`taskType`、`status`、`statusName`、`assignedTo`、`priority`。
- 账单：`id`、`roomNumber`、`guestName`、`status`、`roomAmount`、`serviceAmount`、`totalAmount`、`paidAmount`。

## 冲突和取舍

- 产品文档房态可使用 `VC/OC/RS/VD/CLEANING/OOO`，前端页面使用 `vacant/occupied/reserved/dirty/cleaning/maintenance`。后端内部保留文档码，接口返回前端枚举。
- OTA、真实支付、OCR、短信、Redis 不作为本次落地强依赖，只保留模拟或后续扩展说明。
- 当前工程不修改前端请求层，后端按前端 mock 字段提供 API。

