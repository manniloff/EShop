package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {
    List<PaymentMethodDto> findAll();
    Optional<PaymentMethodDto> findById(Short id);
    Short save(PaymentMethodDto paymentMethodDto);
    Short update(PaymentMethodDto paymentMethodDto, short id);
    Short deleteById(short id);
}
