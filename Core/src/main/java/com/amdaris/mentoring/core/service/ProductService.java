package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll(Pageable pageable);

    Optional<Product> findById(long id);

    long save(Product product);

    long update(Product product, long id);

    long deleteById(long id);

    Optional<Product> findByTitle(String title);

    void clear();
}
