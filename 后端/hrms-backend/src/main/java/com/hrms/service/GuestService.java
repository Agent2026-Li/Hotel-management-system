package com.hrms.service;

import com.hrms.dto.response.GuestResponse;

import java.util.List;

/**
 * 客史业务接口。
 */
public interface GuestService {
    List<GuestResponse> list(String keyword);

    GuestResponse get(String id);
}

