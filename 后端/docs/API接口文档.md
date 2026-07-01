# API 接口文档

Base URL：`http://localhost:8080`

统一响应：`code=0` 为成功；失败时 `code` 为业务错误码，`message` 为原因。

鉴权：除 `/api/auth/login`、`/api/auth/test-accounts` 外，所有 `/api/**` 接口需要 `Authorization: Bearer <token>`。

## 登录

`POST /api/auth/login`

```json
{"username":"admin","password":"123456"}
```

返回 `token/tokenType/username/name/role/expiresInHours`。

## 房态

- `GET /api/rooms?status=vacant&floor=1`
- `GET /api/rooms/{number}`
- `PATCH /api/rooms/{number}/status`
- `POST /api/rooms/{number}/change-room`
- `PATCH /api/rooms/{number}/extend-stay`

```json
{"status":"cleaning","reason":"开始清洁"}
```

换房请求：

```json
{"targetRoomNumber":"106","reason":"客人要求换房"}
```

续住请求：

```json
{"checkout":"2026-07-08","reason":"客人续住"}
```

## 预订

- `GET /api/reservations?status=confirmed`
- `POST /api/reservations`
- `POST /api/reservations/{id}/cancel`

## 入住退房

- `POST /api/checkin`: 传 `reservationId` 为预订入住；传 `roomNumber/name/phone/checkoutDate` 为 Walk-in 入住。
- `POST /api/checkout`: 传 `roomNumber` 或 `checkInId`。

## 账单

- `GET /api/bills`
- `GET /api/bills/{id}`
- `POST /api/bills/{id}/charges`

```json
{"itemName":"迷你吧","category":"MINIBAR","amount":25.00,"quantity":1}
```

## 清洁

- `GET /api/housekeeping/tasks`
- `PATCH /api/housekeeping/tasks/{id}`

```json
{"status":"inspected","remark":"检查通过"}
```

## 客史和报表

- `GET /api/guests?keyword=张`
- `GET /api/guests/{id}`
- `GET /api/reports/occupancy?date=2026-07-01`
- `GET /api/reports/revenue?date=2026-07-01`

完整可执行样例见 `../http/hrms-api.http`。
