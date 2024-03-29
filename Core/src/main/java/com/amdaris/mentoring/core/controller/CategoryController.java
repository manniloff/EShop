package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.dto.CategoryDto;
import com.amdaris.mentoring.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> findAll() {
        log.info("Try to get list of categories");
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> findById(@PathVariable short id) {
        log.info("Try to get category by id - {}", id);
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody CategoryDto categoryDto) {
        log.info("Try to save new category with title - {}", categoryDto.getTitle());
        return new ResponseEntity<>(categoryService.save(categoryDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody CategoryDto categoryDto, @PathVariable short id) {
        log.info("Try to update category with title - {}", categoryDto.getTitle());
        return new ResponseEntity<>(categoryService.update(categoryDto, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable short id) {
        log.info("Try to delete category with id - {}", id);
        categoryService.deleteById(id);
        return ResponseEntity.ok("Category with id - " + id + ", was deleted");
    }
}
