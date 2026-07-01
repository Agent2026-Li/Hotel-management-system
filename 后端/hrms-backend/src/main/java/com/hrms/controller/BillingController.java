package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.common.RoleRequired;
import com.hrms.dto.request.ChargeRequest;
import com.hrms.dto.response.BillResponse;
import com.hrms.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账单管理接口控制器，负责账单查询和消费入账。
 */
@RestController
@RequestMapping("/api/bills")
public class BillingController {
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    /**
     * 查询账单列表，可按状态和房间筛选。
     */
    @Operation(summary = "查询账单列表")
    @GetMapping
    public ApiResponse<List<BillResponse>> list(@RequestParam(required = false) String status,
                                                @RequestParam(required = false) String roomNumber) {
        return ApiResponse.success(billingService.list(status, roomNumber));
    }

    /**
     * 查询账单详情。
     */
    @Operation(summary = "查询账单详情")
    @GetMapping("/{id}")
    public ApiResponse<BillResponse> get(@PathVariable String id) {
        return ApiResponse.success(billingService.get(id));
    }

    /**
     * 给账单新增消费项目。
     */
    @Operation(summary = "消费入账")
    @PostMapping("/{id}/charges")
    @RoleRequired({"ADMIN", "MANAGER", "FRONT_DESK", "FINANCE"})
    public ApiResponse<BillResponse> charge(@PathVariable String id, @Valid @RequestBody ChargeRequest request) {
        return ApiResponse.success(billingService.charge(id, request));
    }
}

