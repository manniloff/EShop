package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.payment.model.PaymentDetails;
import com.amdaris.mentoring.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final List<PaymentService> paymentService;

    //TODO Add kafka and remove this endpoint
    @PostMapping(value = {"", "/"}, produces = "application/json")
    public ResponseEntity<?> kafkaMessage(@RequestBody OrderDetails orderDetails) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setOrderDetails(orderDetails);

        return ResponseEntity.ok(paymentDetails);
    }

    @PostMapping(value = "/pay", produces = "application/json")
    public ResponseEntity<?> paymentForm(@RequestBody PaymentDetails paymentDetails) throws Exception {
        Optional<PaymentService> payService = paymentService.stream()
                .filter(service -> service.getClass().getName().contains(paymentDetails.getMethod()))
                .findFirst();

        if (payService.isPresent()) {
            return ResponseEntity.ok(payService.get().payOrder(paymentDetails));
        }
        return new ResponseEntity<>("No found payment method", HttpStatus.NO_CONTENT);
    }
}
