package com.amdaris.mentoring.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private List<Short> categoryIds;
    private String title;
    private String description;
    private double price;
    private short sale;
}
