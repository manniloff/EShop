package com.amdaris.mentoring.gateway.controller;

import com.amdaris.mentoring.common.dto.BucketDto;
import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.OrderInfo;
import com.amdaris.mentoring.common.model.PaymentInfo;
import com.amdaris.mentoring.common.model.ShipmentInfo;
import com.amdaris.mentoring.gateway.feign.CoreProxy;
import com.amdaris.mentoring.gateway.feign.PaymentProxy;
import com.amdaris.mentoring.gateway.feign.ShipmentProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class GatewayController {
    private final CoreProxy coreProxy;
    private final PaymentProxy paymentProxy;
    private final ShipmentProxy shipmentProxy;

    @PostMapping(value = {"/", ""}, produces = "application/json")
    @CircuitBreaker(name = "orderDetails", fallbackMethod = "fallback")
    @TimeLimiter(name = "core-service")
    @Retry(name = "core-service")
    ResponseEntity<?> checkout(@RequestBody BucketDto bucketDto) {
        UUID transId = UUID.randomUUID();
        log.info("Get order info for transaction id: {}", transId);
        ResponseEntity<OrderInfo> orderInfo = coreProxy.getOrderInfo(bucketDto, transId);
        log.info("Get payment info and create payment for transaction id: {}", transId);
        ResponseEntity<List<PaymentInfo>> paymentInfo = paymentProxy.getPaymentInfo(transId);
        log.info("Get shipment info and create shipment for transaction id: {}", transId);
        ResponseEntity<List<ShipmentInfo>> shipmentInfo = shipmentProxy.getShipmentInfo(transId);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(Objects.requireNonNull(orderInfo.getBody()).getId());
        orderDetails.setTransId(transId);
        orderDetails.setProducts(orderInfo.getBody().getProducts());
        orderDetails.setPayments(paymentInfo.getBody());
        orderDetails.setShipments(shipmentInfo.getBody());

        return ResponseEntity.ok(orderDetails);
    }

    public ResponseEntity<?> fallback(BucketDto bucketDto, RuntimeException runtimeException) {
        return new ResponseEntity<>("Something went wrong, please try again later",
                HttpStatus.GATEWAY_TIMEOUT);
    }
}
