package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.dto.BucketDto;
import com.amdaris.mentoring.core.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class OrderCheckoutController {
    private final OrderService orderService;

    @PostMapping(value = {"/{transId}"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody BucketDto bucket, @PathVariable UUID transId) {
        log.info("Try to save new order for bucket id - {}", bucket.getId());
        return ResponseEntity.ok(orderService.checkout(bucket, transId));
    }
}
