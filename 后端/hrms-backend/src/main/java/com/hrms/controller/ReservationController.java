package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.common.RoleRequired;
import com.hrms.dto.request.ReservationRequest;
import com.hrms.dto.response.ReservationResponse;
import com.hrms.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预订管理接口控制器，负责预订查询、创建和取消。
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * 查询预订列表，可按状态筛选。
     */
    @Operation(summary = "查询预订列表")
    @GetMapping
    @RoleRequired({"ADMIN", "FRONT_DESK"})
    public ApiResponse<List<ReservationResponse>> list(@RequestParam(required = false) String status) {
        return ApiResponse.success(reservationService.list(status));
    }

    /**
     * 创建新预订。
     */
    @Operation(summary = "创建预订")
    @PostMapping
    @RoleRequired({"ADMIN", "FRONT_DESK"})
    public ApiResponse<ReservationResponse> create(@Valid @RequestBody ReservationRequest request) {
        return ApiResponse.success(reservationService.create(request));
    }

    /**
     * 编辑预订。
     */
    @Operation(summary = "编辑预订")
    @PatchMapping("/{id}")
    @RoleRequired({"ADMIN", "FRONT_DESK"})
    public ApiResponse<ReservationResponse> update(@PathVariable String id,
                                                   @Valid @RequestBody ReservationRequest request) {
        return ApiResponse.success(reservationService.update(id, request));
    }

    /**
     * 取消预订。
     */
    @Operation(summary = "取消预订")
    @PostMapping("/{id}/cancel")
    @RoleRequired({"ADMIN", "FRONT_DESK"})
    public ApiResponse<ReservationResponse> cancel(@PathVariable String id) {
        return ApiResponse.success(reservationService.cancel(id));
    }
}
