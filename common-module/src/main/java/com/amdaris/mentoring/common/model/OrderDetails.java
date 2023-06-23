package com.amdaris.mentoring.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private long id;
    private UUID transId;
    private List<ProductInfo> products;
    private List<ShipmentInfo> shipments;
    private List<PaymentInfo> payments;
}

