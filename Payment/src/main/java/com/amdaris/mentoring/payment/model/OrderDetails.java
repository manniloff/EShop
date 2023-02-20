package com.amdaris.mentoring.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private long id;
    private List<ProductDetails> productDetails;
    private double shippingPrice;
}
