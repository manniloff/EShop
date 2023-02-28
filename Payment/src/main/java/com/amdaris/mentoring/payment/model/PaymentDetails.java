package com.amdaris.mentoring.payment.model;

import com.amdaris.mentoring.common.model.OrderDetails;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentDetails {
    private OrderDetails orderDetails;
    private String currency;
    private String method;
    private String details;
}
