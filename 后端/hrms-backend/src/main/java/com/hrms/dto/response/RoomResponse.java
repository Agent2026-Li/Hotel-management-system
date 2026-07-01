package com.hrms.dto.response;

import java.math.BigDecimal;

/**
 * 房间接口响应对象，字段兼容前端房态页面。
 */
public class RoomResponse {
    public String number;
    public Integer floor;
    public String type;
    public String typeName;
    public BigDecimal price;
    public String status;
    public String statusName;
    public GuestBrief guest;
}

