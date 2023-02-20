package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentHistoryDto;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryService {
    List<PaymentHistoryDto> findAll();
    Optional<PaymentHistoryDto> findById(long id);
    Long save(PaymentHistoryDto paymentHistoryDto);
    Long update(PaymentHistoryDto paymentHistoryDto, long id);
    Long deleteById(long id);
}
