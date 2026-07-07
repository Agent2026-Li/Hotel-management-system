package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.common.AuthInterceptor;
import com.hrms.common.CurrentUser;
import com.hrms.common.RoleRequired;
import com.hrms.dto.request.HousekeepingUpdateRequest;
import com.hrms.dto.response.HousekeepingTaskResponse;
import com.hrms.service.HousekeepingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客房清洁接口控制器，负责清洁任务查询和状态流转。
 */
@RestController
@RequestMapping("/api/housekeeping/tasks")
public class HousekeepingController {
    private final HousekeepingService housekeepingService;

    public HousekeepingController(HousekeepingService housekeepingService) {
        this.housekeepingService = housekeepingService;
    }

    /**
     * 查询清洁任务列表。
     */
    @Operation(summary = "查询清洁任务列表")
    @GetMapping
    @RoleRequired({"ADMIN", "MANAGER", "HOUSEKEEPING"})
    public ApiResponse<List<HousekeepingTaskResponse>> list(@RequestParam(required = false) String status,
                                                            @RequestParam(required = false) String roomNumber,
                                                            HttpServletRequest httpRequest) {
        CurrentUser currentUser = (CurrentUser) httpRequest.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        String assignedTo = "HOUSEKEEPING".equals(currentUser.role()) ? currentUser.username() : null;
        return ApiResponse.success(housekeepingService.list(status, roomNumber, assignedTo));
    }

    /**
     * 更新清洁任务状态。
     */
    @Operation(summary = "更新清洁任务状态")
    @PatchMapping("/{id}")
    @RoleRequired({"ADMIN", "MANAGER", "HOUSEKEEPING"})
    public ApiResponse<HousekeepingTaskResponse> update(@PathVariable String id,
                                                        @Valid @RequestBody HousekeepingUpdateRequest request,
                                                        HttpServletRequest httpRequest) {
        CurrentUser currentUser = (CurrentUser) httpRequest.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        return ApiResponse.success(housekeepingService.update(id, request, currentUser));
    }
}
