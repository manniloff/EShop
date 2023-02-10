package com.amdaris.mentoring.payment.model;

import lombok.Data;

@Data
public class PaymentDetails {
    private OrderDetails orderDetails;
    private String currency;
    private String method;
    private String details;
}
