package com.amdaris.mentoring.core.dto.converter;

import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.model.Category;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

public class CategoryConverter {
    public static Function<Category, CategoryDto> toCategoryDto =
            category -> CategoryDto.builder()
                    .id(category.getId())
                    .title(category.getTitle())
                    .build();

    public static Function<CategoryDto, Category> toCategory =
            categoryDto -> Category.builder()
                    .id(Optional.ofNullable(categoryDto.getId()).orElse((short) 0))
                    .title(categoryDto.getTitle())
                    .products(new HashSet<>())
                    .build();
}
