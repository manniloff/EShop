package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/payment/method")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping(value = {"", "/"}, produces = "application/json")
    public ResponseEntity<?> findAll() {
        log.info("Getting payment method list");
        return ResponseEntity.ok(paymentMethodService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findById(@PathVariable short id) {
        log.info("Getting payment method by id");
        return ResponseEntity.ok(paymentMethodService.findById(id));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody PaymentMethodDto paymentMethodDto) {
        log.info("Creating payment method");
        return new ResponseEntity<>(paymentMethodService.save(paymentMethodDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody PaymentMethodDto paymentMethodDto, @PathVariable short id) {
        log.info("Updating payment method with id - ", id);
        return ResponseEntity.ok(paymentMethodService.update(paymentMethodDto, id));
    }

    @DeleteMapping(value = {"/{id}"}, produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable short id) {
        log.info("Deleting payment method by id");
        paymentMethodService.deleteById(id);
        return ResponseEntity.ok("Payment method with id - " + id + ", was deleted");
    }
}
