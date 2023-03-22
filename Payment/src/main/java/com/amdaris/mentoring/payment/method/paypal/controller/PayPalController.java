package com.amdaris.mentoring.payment.method.paypal.controller;

import com.amdaris.mentoring.payment.method.OrderPaymentService;
import com.amdaris.mentoring.payment.method.paypal.model.PaypalPaymentOrder;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment/paypal")
@RequiredArgsConstructor
public class PayPalController {
    private final OrderPaymentService<Payment, PaypalPaymentOrder> paymentService;

    @GetMapping(value = "/pay/cancel")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("Payment failed");
    }

    @GetMapping(value = "/pay/success")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId) throws Exception {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("Payment success");
            }
        return ResponseEntity.ok("Payment pending");
    }
}
