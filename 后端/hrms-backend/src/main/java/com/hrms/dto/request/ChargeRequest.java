package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * 消费入账请求参数。
 */
public class ChargeRequest {
    @NotBlank
    public String itemName;

    public String category;

    @NotNull
    public BigDecimal amount;

    public Integer quantity;
    public String remark;
}
