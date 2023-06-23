package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.service.BucketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;

    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> findAll() {
        log.info("Try to get list of buckets");
        return ResponseEntity.ok(bucketService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        log.info("Try to get bucket by id - {}", id);
        return ResponseEntity.ok(bucketService.findById(id));
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> update(@RequestBody BucketDto bucket, @PathVariable Long id) {
        log.info("Try to update bucket with id- {}", id);
        return new ResponseEntity<>(bucketService.update(bucket, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        log.info("Try to delete bucket with id - {}", id);
        bucketService.deleteById(id);
        return ResponseEntity.ok("Bucket with id - " + id + ", was deleted");
    }
}
