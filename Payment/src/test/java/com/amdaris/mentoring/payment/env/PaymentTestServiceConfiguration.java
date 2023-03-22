package com.amdaris.mentoring.payment.env;

import com.amdaris.mentoring.payment.method.OrderPaymentService;
import com.amdaris.mentoring.payment.method.paypal.model.PaypalPaymentOrder;
import com.paypal.api.payments.Payment;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class PaymentTestServiceConfiguration {
    @Bean
    @Primary
    public OrderPaymentService<Payment, PaypalPaymentOrder> orderPaymentService() {
        return Mockito.mock(OrderPaymentService.class);
    }
}
