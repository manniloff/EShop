package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.payment.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/payment/history")
@RequiredArgsConstructor
public class PaymentHistoryController {
    private final PaymentHistoryService paymentHistoryService;

    @GetMapping(value = {"", "/"}, produces = "application/json")
    public ResponseEntity<?> findAll() {
        log.info("Getting payment list");
        return ResponseEntity.ok(paymentHistoryService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findById(@PathVariable long id) {
        log.info("Getting payment by id");
        return ResponseEntity.ok(paymentHistoryService.findById(id));
    }

    @DeleteMapping(value = {"/{id}"}, produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Deleting payment by id");
        paymentHistoryService.deleteById(id);
        return ResponseEntity.ok("Payment history with id - " + id + ", was deleted");
    }
}
