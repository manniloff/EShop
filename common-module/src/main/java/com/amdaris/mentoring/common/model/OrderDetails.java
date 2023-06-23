package com.amdaris.mentoring.common.model;

import com.amdaris.mentoring.common.dto.PaymentMethodDto;
import com.amdaris.mentoring.common.dto.ShipmentMethodDto;
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
    private List<ProductDetails> products;
    private List<ShipmentMethodDto> shipments;
    private List<PaymentMethodDto> payments;
}

