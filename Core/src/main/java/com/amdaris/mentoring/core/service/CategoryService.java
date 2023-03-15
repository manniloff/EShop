package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(Short id);

    Short save(Category category);

    Category update(Category category, short id);

    Short deleteById(short id);

    Optional<Category> findByTitle(String title);

    void clear();
}
