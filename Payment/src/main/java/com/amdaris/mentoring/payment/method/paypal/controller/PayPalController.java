package com.amdaris.mentoring.payment.method.paypal.controller;

import com.amdaris.mentoring.payment.method.paypal.service.PaypalPaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment/paypal")
@RequiredArgsConstructor
public class PayPalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayPalController.class);

    private final PaypalPaymentService paymentService;

    @GetMapping(value = "/pay/success")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("Payment failed");
    }

    @GetMapping(value = "/pay/cancel")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("Payment success");
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("Payment pending");
    }
}
