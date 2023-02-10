package com.amdaris.mentoring.payment.method.paypal.controller;

import com.amdaris.mentoring.payment.method.paypal.model.PaypalOrder;
import com.amdaris.mentoring.payment.method.paypal.model.PaypalPaymentOrder;
import com.amdaris.mentoring.payment.method.paypal.service.PaypalPaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment/paypal")
@RequiredArgsConstructor
public class PayPalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayPalController.class);
    private final PaypalPaymentService paypalService;

    public static final String SUCCESS_URL = "/payment/paypal/pay/success";
    public static final String CANCEL_URL = "/payment/paypal/pay/cancel";

    private static final String HOST_URL = "http://localhost:3360";

    @PostMapping("/pay")
    public ResponseEntity<String> payment(@ModelAttribute("order") PaypalOrder order) {
        try {
            PaypalPaymentOrder paypalPaymentOrder = new PaypalPaymentOrder(order.getPrice(), order.getCurrency(), order.getMethod(), order.getIntent(),
                    order.getDescription(), HOST_URL + CANCEL_URL, HOST_URL + SUCCESS_URL);

            Payment payment = paypalService.createPayment(paypalPaymentOrder);

            for(Links link : payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok("redirect:"+link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            LOGGER.error("Error occurred on paypal payment process - ", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Nothing happened");
    }

    @GetMapping(value = CANCEL_URL)
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("Payment failed");
    }

    @GetMapping(value = SUCCESS_URL)
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
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
