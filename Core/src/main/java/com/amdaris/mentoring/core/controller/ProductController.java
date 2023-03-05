package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.model.Product;
import com.amdaris.mentoring.core.service.ProductService;
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

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody Product product) {
        log.info("Try to save new product with title - {}", product.getTitle());
        long productId = productService.save(product);
        if (productId != 0) {
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody Product product, @PathVariable long id) {
        log.info("Try to update product with title - {}", product.getTitle());
        long productId = productService.update(product, id);
        if (productId != 0) {
            return new ResponseEntity<>("Updated", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Not Modified", HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete product with id - {}", id);
        productService.deleteById(id);
        return ResponseEntity.ok("Product with id - " + id + ", was deleted");
    }
}
