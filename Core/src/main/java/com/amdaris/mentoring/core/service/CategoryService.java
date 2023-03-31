package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto findById(short id);

    CategoryDto save(CategoryDto category);

    CategoryDto update(CategoryDto category, short id);

    Short deleteById(short id);

    CategoryDto findByTitle(String title);

    void clear();
}
