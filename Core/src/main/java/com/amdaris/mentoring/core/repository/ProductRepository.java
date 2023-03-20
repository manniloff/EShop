package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Optional<Product> findByTitle(String title);

    List<Product> findAllByTitleContaining(String title);
}
