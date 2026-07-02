CREATE DATABASE IF NOT EXISTS hrms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE hrms;

DROP TABLE IF EXISTS bill_item;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS housekeeping_task;
DROP TABLE IF EXISTS check_in_record;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS guest;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS room_type;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(32) NOT NULL UNIQUE,
    role_name VARCHAR(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    real_name VARCHAR(64) NOT NULL,
    enabled TINYINT NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE room_type (
    id VARCHAR(16) PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE room (
    room_number VARCHAR(16) PRIMARY KEY,
    floor INT NOT NULL,
    type_id VARCHAR(16) NOT NULL,
    status VARCHAR(16) NOT NULL COMMENT 'VC/OC/RS/VD/CLEANING/OOO',
    remark VARCHAR(255),
    CONSTRAINT fk_room_type FOREIGN KEY (type_id) REFERENCES room_type(id),
    INDEX idx_room_floor (floor),
    INDEX idx_room_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE guest (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    id_card VARCHAR(32),
    room_number VARCHAR(16),
    checkin DATE,
    checkout DATE,
    total_orders INT NOT NULL DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    member_level VARCHAR(32)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE reservation (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    room_type VARCHAR(16) NOT NULL,
    room_number VARCHAR(16),
    checkin DATE NOT NULL,
    checkout DATE NOT NULL,
    nights INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(32) NOT NULL COMMENT 'confirmed/cancelled/checked_in',
    remark VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_reservation_status (status),
    INDEX idx_reservation_date (checkin, checkout)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE check_in_record (
    id VARCHAR(32) PRIMARY KEY,
    reservation_id VARCHAR(32),
    guest_id VARCHAR(32) NOT NULL,
    room_number VARCHAR(16) NOT NULL,
    checkin_time DATETIME NOT NULL,
    expected_checkout DATE NOT NULL,
    checkout_time DATETIME,
    status VARCHAR(32) NOT NULL COMMENT 'living/checked_out',
    bill_id VARCHAR(32),
    INDEX idx_checkin_room (room_number),
    INDEX idx_checkin_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE housekeeping_task (
    id VARCHAR(32) PRIMARY KEY,
    room_number VARCHAR(16) NOT NULL,
    task_type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL COMMENT 'pending/doing/completed/done',
    assigned_to VARCHAR(64),
    priority VARCHAR(16),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    remark VARCHAR(255),
    INDEX idx_housekeeping_status (status),
    INDEX idx_housekeeping_room (room_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bill (
    id VARCHAR(32) PRIMARY KEY,
    room_number VARCHAR(16) NOT NULL,
    guest_name VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL COMMENT 'unpaid/paid',
    room_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    service_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_bill_room (room_number),
    INDEX idx_bill_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bill_item (
    id VARCHAR(32) PRIMARY KEY,
    bill_id VARCHAR(32) NOT NULL,
    item_name VARCHAR(64) NOT NULL,
    category VARCHAR(32) NOT NULL COMMENT 'ROOM/SERVICE/MINIBAR/OTHER',
    amount DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    remark VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bill_item_bill FOREIGN KEY (bill_id) REFERENCES bill(id),
    INDEX idx_bill_item_bill (bill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

