package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.dto.converter.CategoryConverter;
import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.repository.CategoryRepository;
import com.amdaris.mentoring.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityExistsException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> CategoryConverter.toCategoryDto.apply(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(short id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            return CategoryConverter.toCategoryDto.apply(category.get());
        }
        throw new NoSuchElementException("Category with id - " + id + " doesn't exist!");
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Optional<Category> byTitle = categoryRepository.findByTitle(categoryDto.getTitle());

        if (byTitle.isPresent()) {
            throw new EntityExistsException("Category with title - " + categoryDto.getTitle() + ", exists!");
        }
        Category category = CategoryConverter.toCategory.apply(categoryDto);
        return CategoryConverter.toCategoryDto.apply(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, short id) {
        Optional<Category> byId = categoryRepository.findById(id);
        Category category = CategoryConverter.toCategory.apply(categoryDto);
        if (byId.isPresent()) {
            return CategoryConverter.toCategoryDto.apply(categoryRepository.save(category));
        }
        throw new NoSuchElementException("Category with id - " + id + " doesn't exist!");
    }

    @Override
    public Short deleteById(short id) {
        Optional<Category> byId = categoryRepository.findById(id);

        if (byId.isPresent()) {
            categoryRepository.delete(byId.get());
            return byId.get().getId();
        }
        throw new NoSuchElementException("Category with id - " + id + " doesn't exist!");
    }

    @Override
    public CategoryDto findByTitle(String title) {
        Optional<Category> category = categoryRepository.findByTitle(title);
        if (category.isPresent()) {
            return CategoryConverter.toCategoryDto.apply(category.get());
        } else {
            throw new NoSuchElementException("Category with title - " + title + " doesn't exist!");
        }
    }

    @Override
    public void clear() {
        categoryRepository.deleteAll();
    }
}
