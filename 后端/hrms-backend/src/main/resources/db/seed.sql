USE hrms;

INSERT INTO sys_role (id, role_code, role_name) VALUES
(1, 'ADMIN', '系统管理员'),
(2, 'FRONT_DESK', '前台接待'),
(3, 'MANAGER', '客房经理'),
(4, 'HOUSEKEEPING', '客房清洁员'),
(5, 'FINANCE', '财务人员');

INSERT INTO sys_user (id, username, password, real_name, enabled) VALUES
(1, 'admin', '123456', '系统管理员', 1),
(2, 'frontdesk', '123456', '前台接待', 1),
(3, 'manager', '123456', '客房经理', 1),
(4, 'housekeeper', '123456', '客房清洁员', 1),
(5, 'finance', '123456', '财务人员', 1);

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

INSERT INTO room_type (id, name, price, description) VALUES
('RT001', '标准单人间', 199.00, '单人入住，基础房型'),
('RT002', '标准双人间', 259.00, '双人入住，前端预订常用房型'),
('RT003', '豪华大床房', 399.00, '高价房型，用于报表测试'),
('RT004', '行政套房', 699.00, '套房房型');

INSERT INTO room (room_number, floor, type_id, status, remark)
SELECT CAST(f.floor_no * 100 + n.room_no AS CHAR),
       f.floor_no,
       CASE
           WHEN n.room_no <= 4 THEN 'RT001'
           WHEN n.room_no <= 8 THEN 'RT002'
           WHEN n.room_no <= 11 THEN 'RT003'
           ELSE 'RT004'
       END,
       'VC',
       NULL
FROM (SELECT 1 floor_no UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7) f
CROSS JOIN (SELECT 1 room_no UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14) n;

UPDATE room SET status = 'OC' WHERE room_number IN ('101', '201');
UPDATE room SET status = 'RS' WHERE room_number IN ('102', '309');
UPDATE room SET status = 'VD' WHERE room_number = '103';
UPDATE room SET status = 'CLEANING' WHERE room_number = '104';
UPDATE room SET status = 'OOO' WHERE room_number = '105';

INSERT INTO guest (id, name, phone, id_card, room_number, checkin, checkout, total_orders, total_amount, member_level) VALUES
('G001', '张三', '13800000001', '370100199901010011', '101', '2026-07-01', '2026-07-03', 2, 598.00, '普通会员'),
('G002', '王五', '13800000005', '370100199802020022', '201', '2026-06-30', '2026-07-02', 1, 259.00, '银卡会员');

INSERT INTO reservation (id, name, phone, room_type, room_number, checkin, checkout, nights, amount, status, remark) VALUES
('R20260701001', '李四', '13800000002', 'RT002', '102', '2026-07-02', '2026-07-04', 2, 518.00, 'confirmed', '前端预订测试数据'),
('R20260701002', '赵六', '13800000006', 'RT003', '309', '2026-07-03', '2026-07-05', 2, 798.00, 'confirmed', '报表测试数据');

INSERT INTO bill (id, room_number, guest_name, status, room_amount, service_amount, total_amount, paid_amount) VALUES
('B001', '101', '张三', 'unpaid', 398.00, 0.00, 398.00, 0.00),
('B002', '201', '王五', 'unpaid', 259.00, 0.00, 259.00, 0.00);

INSERT INTO bill_item (id, bill_id, item_name, category, amount, quantity, remark) VALUES
('BI001', 'B001', '房费', 'ROOM', 398.00, 1, '两晚房费'),
('BI002', 'B002', '房费', 'ROOM', 259.00, 1, '一晚房费');

INSERT INTO check_in_record (id, reservation_id, guest_id, room_number, checkin_time, expected_checkout, status, bill_id) VALUES
('CI001', NULL, 'G001', '101', '2026-07-01 14:00:00', '2026-07-03', 'living', 'B001'),
('CI002', NULL, 'G002', '201', '2026-06-30 16:30:00', '2026-07-02', 'living', 'B002');

INSERT INTO housekeeping_task (id, room_number, task_type, status, assigned_to, priority, created_at, remark) VALUES
('HK001', '103', '退房清洁', 'pending', 'housekeeper', 'high', '2026-07-01 10:00:00', '退房后脏房'),
('HK002', '104', '续住清洁', 'doing', 'housekeeper', 'normal', '2026-07-01 09:30:00', '住客要求打扫');

