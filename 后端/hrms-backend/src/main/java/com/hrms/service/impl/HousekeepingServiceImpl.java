package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.common.CurrentUser;
import com.hrms.dto.request.HousekeepingUpdateRequest;
import com.hrms.dto.response.HousekeepingTaskResponse;
import com.hrms.entity.HousekeepingTask;
import com.hrms.mapper.HousekeepingTaskMapper;
import com.hrms.mapper.RoomMapper;
import com.hrms.service.HousekeepingService;
import com.hrms.utils.IdGenerator;
import com.hrms.utils.StatusMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 清洁任务业务实现，负责退房清洁任务和清洁状态流转。
 */
@Service
public class HousekeepingServiceImpl implements HousekeepingService {
    private final HousekeepingTaskMapper taskMapper;
    private final RoomMapper roomMapper;
    private final IdGenerator idGenerator;

    public HousekeepingServiceImpl(HousekeepingTaskMapper taskMapper, RoomMapper roomMapper, IdGenerator idGenerator) {
        this.taskMapper = taskMapper;
        this.roomMapper = roomMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public List<HousekeepingTaskResponse> list(String status, String roomNumber, String assignedTo) {
        return taskMapper.selectTaskList(status, roomNumber, assignedTo).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public HousekeepingTaskResponse createDirtyTask(String roomNumber, String remark) {
        HousekeepingTask task = new HousekeepingTask();
        task.id = idGenerator.housekeepingId();
        task.roomNumber = roomNumber;
        task.taskType = "退房清洁";
        task.status = "pending";
        task.assignedTo = "housekeeper";
        task.priority = "high";
        task.createdAt = LocalDateTime.now();
        task.remark = remark;
        taskMapper.insert(task);
        return toResponse(taskMapper.getById(task.id));
    }

    @Override
    @Transactional
    public HousekeepingTaskResponse update(String id, HousekeepingUpdateRequest request, CurrentUser currentUser) {
        HousekeepingTask task = taskMapper.getById(id);
        if (task == null) {
            throw new BusinessException(404, "清洁任务不存在");
        }
        if ("HOUSEKEEPING".equals(currentUser.role()) && !currentUser.username().equals(task.assignedTo)) {
            throw new BusinessException(403, "只能处理分配给自己的清洁任务");
        }
        String status = request.status.trim().toLowerCase();
        String assignedTo = task.assignedTo;
        if (request.assignedTo != null && !request.assignedTo.isBlank()) {
            if (!"ADMIN".equals(currentUser.role()) && !"MANAGER".equals(currentUser.role())) {
                throw new BusinessException(403, "当前账号无权限分配清洁任务");
            }
            assignedTo = request.assignedTo.trim();
        }
        LocalDateTime completedAt = task.completedAt;
        switch (status) {
            case "pending" -> {
            }
            case "doing" -> roomMapper.updateStatus(task.roomNumber, "CLEANING");
            case "completed" -> completedAt = LocalDateTime.now();
            case "inspected", "done" -> {
                status = "done";
                completedAt = LocalDateTime.now();
                roomMapper.updateStatus(task.roomNumber, "VC");
            }
            default -> throw new BusinessException(400, "不支持的清洁任务状态: " + request.status);
        }
        String remark = request.remark == null ? task.remark : request.remark;
        taskMapper.updateStatus(id, status, assignedTo, completedAt, remark);
        return toResponse(taskMapper.getById(id));
    }

    private HousekeepingTaskResponse toResponse(HousekeepingTask task) {
        HousekeepingTaskResponse response = new HousekeepingTaskResponse();
        response.id = task.id;
        response.roomNumber = task.roomNumber;
        response.taskType = task.taskType;
        response.status = task.status;
        response.statusName = StatusMapper.housekeepingStatusName(task.status);
        response.assignedTo = task.assignedTo;
        response.priority = task.priority;
        response.createdAt = task.createdAt;
        response.completedAt = task.completedAt;
        response.remark = task.remark;
        return response;
    }
}
