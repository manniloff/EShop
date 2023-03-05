package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.model.Category;
import com.amdaris.mentoring.core.repository.CategoryRepository;
import com.amdaris.mentoring.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Short id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            return category;
        }
        throw new NoSuchElementException("Category with id - " + id + " doesn't exist!");
    }

    @Override
    public Short save(Category category) {
        Optional<Category> byTitle = categoryRepository.findByTitle(category.getTitle());

        if (byTitle.isPresent()) {
            throw new EntityExistsException("Category with title - " + category.getTitle() + ", exists!");
        }

        return categoryRepository.save(category).getId();
    }

    @Override
    public Short update(Category category, short id) {
        Optional<Category> byId = categoryRepository.findById(id);

        if (byId.isPresent()) {
            return categoryRepository.save(category).getId();
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
    @Transactional
    public Optional<Category> findByTitle(String title) {
        Optional<Category> category = categoryRepository.findByTitle(title);
        if (category.isPresent()) {
            return category;
        } else {
            throw new NoSuchElementException("Category with title - " + title + " doesn't exist!");
        }
    }

    @Override
    public void clear() {
        categoryRepository.deleteAll();
    }
}
