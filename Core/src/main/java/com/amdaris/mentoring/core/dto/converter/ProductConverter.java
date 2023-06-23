package com.amdaris.mentoring.core.dto.converter;

import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductConverter {
    public static Function<Product, ProductDto> toProductDto =
            product -> ProductDto.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .sale(product.getSale())
                    .categoryIds(product.getCategories().stream().map(Category::getId).collect(Collectors.toList()))
                    .build();

    public static Function<ProductDto, Product> toProduct =
            productDto -> Product.builder()
                    .id(Optional.ofNullable(productDto.getId()).orElse(0L))
                    .title(productDto.getTitle())
                    .price(productDto.getPrice())
                    .description(productDto.getDescription())
                    .sale(productDto.getSale())
                    .categories(Optional.ofNullable(productDto.getCategoryIds())
                            .orElse(List.of()).stream().map(Category::new).collect(Collectors.toSet()))
                    .build();
}
