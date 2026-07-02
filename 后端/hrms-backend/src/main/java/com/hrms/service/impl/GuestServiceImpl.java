package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.dto.response.GuestResponse;
import com.hrms.entity.Guest;
import com.hrms.mapper.GuestMapper;
import com.hrms.service.GuestService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客史业务实现，负责客户档案查询。
 */
@Service
public class GuestServiceImpl implements GuestService {
    private final GuestMapper guestMapper;

    public GuestServiceImpl(GuestMapper guestMapper) {
        this.guestMapper = guestMapper;
    }

    @Override
    public List<GuestResponse> list(String keyword) {
        return guestMapper.selectGuestList(keyword).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public GuestResponse get(String id) {
        Guest guest = guestMapper.getById(id);
        if (guest == null) {
            throw new BusinessException(404, "客史不存在");
        }
        return toResponse(guest);
    }

    private GuestResponse toResponse(Guest guest) {
        GuestResponse response = new GuestResponse();
        response.id = guest.id;
        response.name = guest.name;
        response.phone = guest.phone;
        response.idCard = guest.idCard;
        response.roomNumber = guest.roomNumber;
        response.checkin = guest.checkin;
        response.checkout = guest.checkout;
        response.totalOrders = guest.totalOrders;
        response.totalAmount = guest.totalAmount;
        response.memberLevel = guest.memberLevel;
        return response;
    }
}

