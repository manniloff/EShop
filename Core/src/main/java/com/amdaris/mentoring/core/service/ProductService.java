package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.criteria.ProductSearchCriteria;
import com.amdaris.mentoring.core.util.PageView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDto> findByCriteria(PageView pageView, ProductSearchCriteria productSearchCriteria);

    List<ProductDto> findAll(Pageable pageable);

    List<ProductDto> findAllPageableAndSortAndFilter(PageView pageView);

    ProductDto findById(long id);

    ProductDto save(ProductDto product);

    ProductDto update(ProductDto product, long id);

    long findProductId(String title);

    long deleteById(long id);

    ProductDto findByTitle(String title);

    List<ProductDto> findAllByTitle(String title);

    void clear();
}
