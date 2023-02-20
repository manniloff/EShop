package com.amdaris.mentoring.payment.method;

public interface OrderPaymentService<T, R> {
    T createPayment(R order) throws Exception;
    T executePayment(String paymentId, String payerId) throws Exception;
}
