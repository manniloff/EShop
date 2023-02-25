package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.core.service.OrderDetailsMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderDetailsMessageSender orderDetailsMessageSender;

    @PostMapping(value = "/send/to/pay")
    public ResponseEntity<?> sendOrderToPay(@RequestBody OrderDetails orderDetails) {
        orderDetailsMessageSender.send(orderDetails);
        return ResponseEntity.ok("message was sent to kafka");
    }
}
