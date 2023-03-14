package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Short> {
    Optional<Category> findByTitle(String title);
}
