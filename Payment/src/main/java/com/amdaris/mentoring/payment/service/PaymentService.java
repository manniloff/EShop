package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.model.PaymentDetails;

public interface PaymentService {
    String payOrder(PaymentDetails orderPaymentDetails) throws Exception;
}
