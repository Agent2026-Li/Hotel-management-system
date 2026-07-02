package com.hrms.service;

import com.hrms.dto.request.ReservationRequest;
import com.hrms.dto.response.ReservationResponse;

import java.util.List;

/**
 * 预订业务接口。
 */
public interface ReservationService {
    List<ReservationResponse> list(String status);

    ReservationResponse create(ReservationRequest request);

    ReservationResponse cancel(String id);
}

