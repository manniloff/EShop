package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodDto> findAll();

    PaymentMethodDto findById(Short id);

    Short save(PaymentMethodDto paymentMethodDto);

    Short update(PaymentMethodDto paymentMethodDto, short id);

    Short deleteById(short id);

    PaymentMethod findByTitle(String title);

    void clear();
}
