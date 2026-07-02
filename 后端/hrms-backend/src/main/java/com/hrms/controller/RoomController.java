package com.hrms.controller;

import com.hrms.common.ApiResponse;
import com.hrms.common.RoleRequired;
import com.hrms.dto.request.ChangeRoomRequest;
import com.hrms.dto.request.ExtendStayRequest;
import com.hrms.dto.request.RoomStatusUpdateRequest;
import com.hrms.dto.response.RoomResponse;
import com.hrms.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 房态管理接口控制器，负责房间查询、房态修改、换房和续住。
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * 查询房间列表，可按房态和楼层筛选。
     */
    @Operation(summary = "查询房间列表")
    @GetMapping
    public ApiResponse<List<RoomResponse>> list(@RequestParam(required = false) String status,
                                                @RequestParam(required = false) Integer floor) {
        return ApiResponse.success(roomService.list(status, floor));
    }

    /**
     * 查询单个房间详情。
     */
    @Operation(summary = "查询房间详情")
    @GetMapping("/{number}")
    public ApiResponse<RoomResponse> get(@PathVariable String number) {
        return ApiResponse.success(roomService.get(number));
    }

    /**
     * 修改房间状态。
     */
    @Operation(summary = "修改房间状态")
    @PatchMapping("/{number}/status")
    @RoleRequired({"ADMIN", "MANAGER", "FRONT_DESK", "HOUSEKEEPING"})
    public ApiResponse<RoomResponse> updateStatus(@PathVariable String number, @Valid @RequestBody RoomStatusUpdateRequest request) {
        return ApiResponse.success(roomService.updateStatus(number, request));
    }

    /**
     * 办理换房。
     */
    @Operation(summary = "办理换房")
    @PostMapping("/{number}/change-room")
    @RoleRequired({"ADMIN", "MANAGER", "FRONT_DESK"})
    public ApiResponse<RoomResponse> changeRoom(@PathVariable String number, @Valid @RequestBody ChangeRoomRequest request) {
        return ApiResponse.success(roomService.changeRoom(number, request));
    }

    /**
     * 办理续住。
     */
    @Operation(summary = "办理续住")
    @PatchMapping("/{number}/extend-stay")
    @RoleRequired({"ADMIN", "MANAGER", "FRONT_DESK"})
    public ApiResponse<RoomResponse> extendStay(@PathVariable String number, @Valid @RequestBody ExtendStayRequest request) {
        return ApiResponse.success(roomService.extendStay(number, request));
    }
}

