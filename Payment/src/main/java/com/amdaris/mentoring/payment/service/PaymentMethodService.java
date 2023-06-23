package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.model.PaymentMethod;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodService {
    List<PaymentMethodDto> findAll();

    List<PaymentMethodDto> findInfo(UUID transId);

    PaymentMethodDto findById(short id);

    PaymentMethod save(PaymentMethodDto paymentMethodDto);

    PaymentMethod update(PaymentMethodDto paymentMethodDto, short id);

    short deleteById(short id);

    PaymentMethod findByTitle(String title);

    boolean existsByTitle(String title);

    void clear();
}
