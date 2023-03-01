package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentHistoryDto;
import com.amdaris.mentoring.payment.model.PaymentHistory;

import java.util.List;

public interface PaymentHistoryService {
    List<PaymentHistoryDto> findAll();

    PaymentHistoryDto findById(long id);

    Long save(PaymentHistoryDto paymentHistoryDto);

    Long update(PaymentHistoryDto paymentHistoryDto, long id);

    Long deleteById(long id);

    PaymentHistory findByOrderId(long orderId);

    void clear();
}
