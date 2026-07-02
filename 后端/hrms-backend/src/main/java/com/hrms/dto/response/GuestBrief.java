package com.hrms.dto.response;

import java.time.LocalDate;

/**
 * 房间详情中的简要客人信息。
 */
public class GuestBrief {
    public String name;
    public String phone;
    public LocalDate checkin;
    public LocalDate checkout;
}

