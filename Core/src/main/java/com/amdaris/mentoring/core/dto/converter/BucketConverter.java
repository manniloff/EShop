package com.amdaris.mentoring.core.dto.converter;

import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.model.Bucket;
import com.amdaris.mentoring.core.model.Product;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BucketConverter {
    public static Function<Bucket, BucketDto> toBucketDto =
            bucket -> BucketDto.builder()
                    .id(bucket.getId())
                    .productIds(bucket.getProducts().stream()
                            .map(Product::getId)
                            .collect(Collectors.toMap(id -> id, id -> 1)))
                    .build();

    public static Function<BucketDto, Bucket> toBucket =
            bucketDto -> Bucket.builder()
                    .id(bucketDto.getId())
                    .products(bucketDto.getProductIds() != null ? bucketDto.getProductIds().keySet().stream()
                            .map(id -> {
                                Product product = new Product();
                                product.setId(id);
                                return product;
                            })
                            .collect(Collectors.toList()) : List.of())
                    .build();
}
