package com.hrms.dto.response;

/**
 * 退房结算结果，返回入住记录、账单和自动生成的清洁任务。
 */
public class CheckOutResult {
    public CheckInRecordResponse checkIn;
    public BillResponse bill;
    public HousekeepingTaskResponse housekeepingTask;

    public CheckOutResult(CheckInRecordResponse checkIn, BillResponse bill, HousekeepingTaskResponse housekeepingTask) {
        this.checkIn = checkIn;
        this.bill = bill;
        this.housekeepingTask = housekeepingTask;
    }
}

