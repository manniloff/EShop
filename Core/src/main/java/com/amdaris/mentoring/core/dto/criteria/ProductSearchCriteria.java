package com.amdaris.mentoring.core.dto.criteria;

import com.amdaris.mentoring.core.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchCriteria {
    private List<Category> categories;
    private String title;
    private String description;
    private double minPrice;
    private double maxPrice;
    private short minSale;
    private short maxSale;
}
