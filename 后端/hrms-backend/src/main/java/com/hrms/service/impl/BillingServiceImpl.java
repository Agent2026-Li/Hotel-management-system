package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.dto.request.ChargeRequest;
import com.hrms.dto.response.BillItemResponse;
import com.hrms.dto.response.BillResponse;
import com.hrms.entity.Bill;
import com.hrms.entity.BillItem;
import com.hrms.mapper.BillItemMapper;
import com.hrms.mapper.BillMapper;
import com.hrms.service.BillingService;
import com.hrms.utils.AmountUtils;
import com.hrms.utils.IdGenerator;
import com.hrms.utils.StatusMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账单业务实现，负责账单查询、消费入账和结算。
 */
@Service
public class BillingServiceImpl implements BillingService {
    private final BillMapper billMapper;
    private final BillItemMapper billItemMapper;
    private final IdGenerator idGenerator;

    public BillingServiceImpl(BillMapper billMapper, BillItemMapper billItemMapper, IdGenerator idGenerator) {
        this.billMapper = billMapper;
        this.billItemMapper = billItemMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public List<BillResponse> list(String status, String roomNumber) {
        return billMapper.selectBillList(status, roomNumber).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BillResponse get(String id) {
        return toResponse(getEntity(id));
    }

    @Override
    @Transactional
    public BillResponse charge(String id, ChargeRequest request) {
        Bill bill = getEntity(id);
        BillItem item = new BillItem();
        item.id = idGenerator.billItemId();
        item.billId = bill.id;
        item.itemName = request.itemName;
        item.category = request.category == null || request.category.isBlank() ? "SERVICE" : request.category;
        item.amount = request.amount;
        item.quantity = request.quantity == null ? 1 : request.quantity;
        item.remark = request.remark;
        billItemMapper.insert(item);
        recalculateAndSave(bill);
        return get(id);
    }

    @Override
    @Transactional
    public BillResponse pay(String id, BigDecimal paidAmount) {
        Bill bill = getEntity(id);
        BigDecimal paid = paidAmount == null ? bill.totalAmount : paidAmount;
        bill.paidAmount = paid;
        bill.status = AmountUtils.billStatus(bill.totalAmount, paid);
        billMapper.updatePayment(bill.id, paid, bill.status);
        return get(id);
    }

    private Bill getEntity(String id) {
        Bill bill = billMapper.getById(id);
        if (bill == null) {
            throw new BusinessException(404, "账单不存在");
        }
        return bill;
    }

    private void recalculateAndSave(Bill bill) {
        List<BillItem> items = billItemMapper.listByBillId(bill.id);
        bill.roomAmount = AmountUtils.roomAmount(items);
        bill.serviceAmount = AmountUtils.serviceAmount(items);
        bill.totalAmount = bill.roomAmount.add(bill.serviceAmount);
        bill.paidAmount = bill.paidAmount == null ? BigDecimal.ZERO : bill.paidAmount;
        bill.status = AmountUtils.billStatus(bill.totalAmount, bill.paidAmount);
        billMapper.updateAmounts(bill);
    }

    private BillResponse toResponse(Bill bill) {
        List<BillItem> items = billItemMapper.listByBillId(bill.id);
        BillResponse response = new BillResponse();
        response.id = bill.id;
        response.roomNumber = bill.roomNumber;
        response.guestName = bill.guestName;
        response.status = bill.status;
        response.statusName = StatusMapper.billStatusName(bill.status);
        response.roomAmount = bill.roomAmount;
        response.serviceAmount = bill.serviceAmount;
        response.totalAmount = bill.totalAmount;
        response.paidAmount = bill.paidAmount;
        response.balanceAmount = bill.totalAmount.subtract(bill.paidAmount == null ? BigDecimal.ZERO : bill.paidAmount);
        response.items = items.stream().map(this::toItemResponse).toList();
        return response;
    }

    private BillItemResponse toItemResponse(BillItem item) {
        BillItemResponse response = new BillItemResponse();
        response.id = item.id;
        response.billId = item.billId;
        response.itemName = item.itemName;
        response.category = item.category;
        response.amount = item.amount;
        response.quantity = item.quantity;
        response.remark = item.remark;
        response.createdAt = item.createdAt;
        return response;
    }
}

