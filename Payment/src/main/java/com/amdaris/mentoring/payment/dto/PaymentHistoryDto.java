package com.amdaris.mentoring.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PaymentHistoryDto {
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String status;
    private Long orderId;
}
