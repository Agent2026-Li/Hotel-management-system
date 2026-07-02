package com.hrms.service;

import com.hrms.dto.request.ChangeRoomRequest;
import com.hrms.dto.request.ExtendStayRequest;
import com.hrms.dto.request.RoomStatusUpdateRequest;
import com.hrms.dto.response.RoomResponse;

import java.util.List;

/**
 * 房态业务接口。
 */
public interface RoomService {
    List<RoomResponse> list(String status, Integer floor);

    RoomResponse get(String number);

    RoomResponse updateStatus(String number, RoomStatusUpdateRequest request);

    RoomResponse changeRoom(String number, ChangeRoomRequest request);

    RoomResponse extendStay(String number, ExtendStayRequest request);
}

