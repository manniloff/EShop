package com.amdaris.mentoring.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentMethodDto {
    private String title;
    private String details;
}
