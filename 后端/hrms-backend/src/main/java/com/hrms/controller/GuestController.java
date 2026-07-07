package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.common.RoleRequired;
import com.hrms.dto.response.GuestResponse;
import com.hrms.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客史接口控制器，负责客户档案查询。
 */
@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * 查询客史列表。
     */
    @Operation(summary = "查询客史列表")
    @GetMapping
    @RoleRequired({"ADMIN", "FRONT_DESK", "MANAGER"})
    public ApiResponse<List<GuestResponse>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(guestService.list(keyword));
    }

    /**
     * 查询客史详情。
     */
    @Operation(summary = "查询客史详情")
    @GetMapping("/{id}")
    @RoleRequired({"ADMIN", "FRONT_DESK", "MANAGER"})
    public ApiResponse<GuestResponse> get(@PathVariable String id) {
        return ApiResponse.success(guestService.get(id));
    }
}
