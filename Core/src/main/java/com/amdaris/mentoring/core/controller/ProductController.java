package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.dto.ProductDto;
import com.amdaris.mentoring.core.dto.criteria.ProductSearchCriteria;
import com.amdaris.mentoring.core.service.ProductService;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/filter", produces = "application/json")
    ResponseEntity<?> findByCriteriaAll(PageView pageView, ProductSearchCriteria criteria) {
        log.info("Try to get list of products by criteria");
        return ResponseEntity.ok(productService.findByCriteria(pageView, criteria));
    }

    @GetMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "id") String sortBy) {
        log.info("Try to get list of products");
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> findById(@PathVariable long id) {
        log.info("Try to get product by id - {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping(value = "/title/{title}", produces = "application/json")
    ResponseEntity<?> findByTitle(@PathVariable String title) {
        log.info("Try to get products by title - {}", title);
        return ResponseEntity.ok(productService.findAllByTitle(title));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody ProductDto productDto) {
        log.info("Try to save new product with title - {}", productDto.getTitle());
        return new ResponseEntity<>(productService.save(productDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody ProductDto productDto, @PathVariable long id) {
        log.info("Try to update product with title - {}", productDto.getTitle());
        return new ResponseEntity<>(productService.update(productDto, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete product with id - {}", id);
        productService.deleteById(id);
        return ResponseEntity.ok("Product with id - " + id + ", was deleted");
    }
}
