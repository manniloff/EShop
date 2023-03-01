package com.amdaris.mentoring.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistoryDto {
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String status;
    private Long orderId;
}
