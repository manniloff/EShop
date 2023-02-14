package com.amdaris.mentoring.payment.method.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaypalOrder {
    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
