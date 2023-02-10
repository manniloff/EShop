package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.exception.EntityExistsException;
import com.amdaris.mentoring.payment.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment/method")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentMethodController.class);

    @GetMapping(value = {"", "/"}, produces = "application/json")
    public ResponseEntity<?> findAll() {
        try {
            LOGGER.info("Getting payment method list");
            return ResponseEntity.ok(paymentMethodService.findAll());
        } catch (Exception e) {
            LOGGER.error("Exception on getting payment method list: ", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findById(@PathVariable short id) {
        try {
            LOGGER.info("Getting payment method by id");
            return ResponseEntity.ok(paymentMethodService.findById(id));
        } catch (Exception e) {
            LOGGER.error("Exception on getting payment method by id: ", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody PaymentMethodDto paymentMethodDto) {
        try {
            LOGGER.info("Creating payment method");
            short id = paymentMethodService.save(paymentMethodDto);
            if (id != 0) {
                return new ResponseEntity<>("Created",HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error("Exception on creating payment method: ", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody PaymentMethodDto paymentMethodDto,@PathVariable short id) {
        try {
            LOGGER.info("Updating payment method with id - ", id);
            Short paymentMethodId = paymentMethodService.update(paymentMethodDto, id);
            if(paymentMethodId != 0){
                return ResponseEntity.ok("Updated");
            }
            return new ResponseEntity<>("Not Modified", HttpStatus.NOT_MODIFIED);
        } catch (EntityExistsException e) {
            LOGGER.error("Exception on updating payment method: ", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping(value = {"/{id}"}, produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable byte id) {
        try {
            LOGGER.info("Deleting payment method by id");
            paymentMethodService.deleteById(id);
            return ResponseEntity.ok("Payment method with id - " + id + ", was deleted");
        } catch (Exception e) {
            LOGGER.error("Exception on deleting payment method by id: ", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
