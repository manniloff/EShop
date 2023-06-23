package com.amdaris.mentoring.core.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSearchCriteria {
    private LocalDateTime startPeriod = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    private LocalDateTime endPeriod = LocalDateTime.now().withNano(0);
    private List<String> statuses;
    private List<Long> productIds;
    private double minOrderPrice;
    private double maxOrderPrice;
}
