package com.amdaris.mentoring.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketDetails {
    private long id;

    private Map<Long, Integer> productIds;
}