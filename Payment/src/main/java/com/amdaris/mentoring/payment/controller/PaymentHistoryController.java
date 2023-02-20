package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.payment.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment/history")
@RequiredArgsConstructor
public class PaymentHistoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentHistoryController.class);
    private final PaymentHistoryService paymentHistoryService;

    @GetMapping(value = {"", "/"}, produces = "application/json")
    public ResponseEntity<?> findAll() {
            LOGGER.info("Getting payment list");
            return ResponseEntity.ok(paymentHistoryService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findById(@PathVariable long id) {
            LOGGER.info("Getting payment by id");
            return ResponseEntity.ok(paymentHistoryService.findById(id));
    }

    @DeleteMapping(value = {"/{id}"}, produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
            LOGGER.info("Deleting payment by id");
            paymentHistoryService.deleteById(id);
            return ResponseEntity.ok("Payment with id - " + id + ", was deleted");
    }
}
