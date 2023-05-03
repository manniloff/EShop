package com.amdaris.mentoring.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private long id;

    private long userId;

    private UUID transId;

    private LocalDateTime creationDate;

    private String status;

    private List<Long> products;

    private double orderPrice;
}
