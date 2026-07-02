package com.hrms.dto.response;

/**
 * 入住办理结果，返回入住记录、账单和最新房间信息。
 */
public class CheckInResult {
    public CheckInRecordResponse checkIn;
    public BillResponse bill;
    public RoomResponse room;

    public CheckInResult(CheckInRecordResponse checkIn, BillResponse bill, RoomResponse room) {
        this.checkIn = checkIn;
        this.bill = bill;
        this.room = room;
    }
}

