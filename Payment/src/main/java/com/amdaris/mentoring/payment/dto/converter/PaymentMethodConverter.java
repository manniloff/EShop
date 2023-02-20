package com.amdaris.mentoring.payment.dto.converter;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.model.PaymentMethod;

import java.util.function.Function;

public class PaymentMethodConverter {
    public static Function<PaymentMethod, PaymentMethodDto> toPaymentMethodDto =
            paymentMethod -> PaymentMethodDto.builder()
                    .title(paymentMethod.getTitle())
                    .details(paymentMethod.getDetails())
                    .build();

    public static Function<PaymentMethodDto, PaymentMethod> toPaymentMethod =
            paymentMethodDto -> PaymentMethod.builder()
                    .title(paymentMethodDto.getTitle())
                    .details(paymentMethodDto.getDetails())
                    .build();
}
