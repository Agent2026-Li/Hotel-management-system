package com.hrms.service;

import com.hrms.dto.request.HousekeepingUpdateRequest;
import com.hrms.dto.response.HousekeepingTaskResponse;

import java.util.List;

/**
 * 清洁任务业务接口。
 */
public interface HousekeepingService {
    List<HousekeepingTaskResponse> list(String status, String roomNumber);

    HousekeepingTaskResponse createDirtyTask(String roomNumber, String remark);

    HousekeepingTaskResponse update(String id, HousekeepingUpdateRequest request);
}

