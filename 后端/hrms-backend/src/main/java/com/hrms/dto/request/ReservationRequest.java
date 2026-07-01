package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * 新增预订请求参数。
 */
public class ReservationRequest {
    @NotBlank
    public String name;

    @NotBlank
    public String phone;

    @NotBlank
    public String roomType;

    public String roomNumber;

    @NotNull
    public LocalDate checkin;

    @NotNull
    public LocalDate checkout;

    public String remark;
}
