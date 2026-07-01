# 03 API 契约

## 统一响应

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

错误码：`400` 参数错误，`401` 未登录或 token 失效，`403` 无权限，`404` 资源不存在，`409` 业务冲突，`500` 服务异常。

## 鉴权

- 登录：`POST /api/auth/login`
- Header：`Authorization: Bearer <token>`
- 测试账号接口 `GET /api/auth/test-accounts` 不需要鉴权。

## 接口清单

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录 |
| GET | `/api/auth/test-accounts` | 测试账号 |
| GET | `/api/rooms` | 房态列表，支持 `status/floor` |
| GET | `/api/rooms/{number}` | 房间详情 |
| PATCH | `/api/rooms/{number}/status` | 修改房态 |
| POST | `/api/rooms/{number}/change-room` | 换房 |
| PATCH | `/api/rooms/{number}/extend-stay` | 续住 |
| GET | `/api/reservations` | 预订列表 |
| POST | `/api/reservations` | 新增预订 |
| POST | `/api/reservations/{id}/cancel` | 取消预订 |
| POST | `/api/checkin` | 预订入住或 Walk-in 入住 |
| POST | `/api/checkout` | 退房结算 |
| GET | `/api/bills` | 账单列表 |
| GET | `/api/bills/{id}` | 账单详情 |
| POST | `/api/bills/{id}/charges` | 消费入账 |
| GET | `/api/housekeeping/tasks` | 清洁任务列表 |
| PATCH | `/api/housekeeping/tasks/{id}` | 更新清洁任务 |
| GET | `/api/guests` | 客史列表 |
| GET | `/api/guests/{id}` | 客史详情 |
| GET | `/api/reports/occupancy` | 入住率报表 |
| GET | `/api/reports/revenue` | 营收报表 |

## 字段约定

房态返回前端枚举：`vacant/occupied/reserved/dirty/cleaning/maintenance`。内部可对应：`VC/OC/RS/VD/CLEANING/OOO`。

房间响应示例：

```json
{
  "number": "101",
  "floor": 1,
  "type": "RT001",
  "typeName": "标准单人间",
  "price": 199.00,
  "status": "occupied",
  "statusName": "入住",
  "guest": {"name": "张三", "phone": "13800000001"}
}
```

新增预订请求：

```json
{
  "name": "测试预订",
  "phone": "13800001111",
  "roomType": "RT002",
  "checkin": "2026-07-06",
  "checkout": "2026-07-08"
}
```

换房请求：

```json
{
  "targetRoomNumber": "106",
  "reason": "客人要求换房"
}
```

续住请求：

```json
{
  "checkout": "2026-07-08",
  "reason": "客人续住"
}
```
