package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.common.RoleRequired;
import com.hrms.dto.request.CheckInRequest;
import com.hrms.dto.request.CheckOutRequest;
import com.hrms.dto.response.CheckInResult;
import com.hrms.dto.response.CheckOutResult;
import com.hrms.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 入住退房接口控制器，负责入住办理和退房结算。
 */
@RestController
public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    /**
     * 办理入住，支持 Walk-in 和预订入住。
     */
    @Operation(summary = "办理入住")
    @PostMapping("/api/checkin")
    @RoleRequired({"ADMIN", "FRONT_DESK"})
    public ApiResponse<CheckInResult> checkIn(@RequestBody CheckInRequest request) {
        return ApiResponse.success(checkInService.checkIn(request));
    }

    /**
     * 办理退房结算。
     */
    @Operation(summary = "办理退房")
    @PostMapping("/api/checkout")
    @RoleRequired({"ADMIN", "FRONT_DESK", "FINANCE"})
    public ApiResponse<CheckOutResult> checkOut(@RequestBody CheckOutRequest request) {
        return ApiResponse.success(checkInService.checkOut(request));
    }
}
