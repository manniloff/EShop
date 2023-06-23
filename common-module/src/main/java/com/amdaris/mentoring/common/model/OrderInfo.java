package com.amdaris.mentoring.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private long id;
    private List<ProductInfo> products;
}
