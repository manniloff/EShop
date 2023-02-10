package com.amdaris.mentoring.payment.method.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPaymentOrder {
    Double total;
    String currency;
    String method;
    String intent;
    String description;
    String cancelUrl;
    String successUrl;
}
