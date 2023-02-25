package com.amdaris.mentoring.payment.dto.converter;

import com.amdaris.mentoring.payment.model.PaymentHistory;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.model.PaymentStatus;
import com.amdaris.mentoring.payment.dto.PaymentHistoryDto;

import java.util.function.Function;

public class PaymentConverter {
    public static Function<PaymentHistory, PaymentHistoryDto> toPaymentDto =
            paymentHistory -> PaymentHistoryDto.builder()
                    .paymentMethod(paymentHistory.getPaymentMethod().getTitle())
                    .paymentDate(paymentHistory.getPaymentDate())
                    .orderId(paymentHistory.getOrderId())
                    .status(paymentHistory.getStatus().toString())
                    .build();

    public static Function<PaymentHistoryDto, PaymentHistory> toPayment =
            paymentHistoryDto -> PaymentHistory.builder()
                    .paymentDate(paymentHistoryDto.getPaymentDate())
                    .paymentMethod(PaymentMethod.builder().title(paymentHistoryDto.getPaymentMethod()).build())
                    .status(PaymentStatus.valueOf(paymentHistoryDto.getStatus()))
                    .orderId(paymentHistoryDto.getOrderId())
                    .build();

}
