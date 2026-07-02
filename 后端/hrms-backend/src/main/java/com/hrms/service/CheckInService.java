package com.hrms.service;

import com.hrms.dto.request.CheckInRequest;
import com.hrms.dto.request.CheckOutRequest;
import com.hrms.dto.response.CheckInResult;
import com.hrms.dto.response.CheckOutResult;

/**
 * 入住和退房业务接口。
 */
public interface CheckInService {
    CheckInResult checkIn(CheckInRequest request);

    CheckOutResult checkOut(CheckOutRequest request);
}

