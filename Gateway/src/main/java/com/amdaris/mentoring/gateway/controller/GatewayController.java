package com.amdaris.mentoring.gateway.controller;

import com.amdaris.mentoring.common.dto.PaymentMethodDto;
import com.amdaris.mentoring.common.dto.ShipmentMethodDto;
import com.amdaris.mentoring.common.model.BucketDetails;
import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.OrderInfo;
import com.amdaris.mentoring.gateway.feign.CoreProxy;
import com.amdaris.mentoring.gateway.feign.PaymentProxy;
import com.amdaris.mentoring.gateway.feign.ShipmentProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class GatewayController {
    private final CoreProxy coreProxy;
    private final PaymentProxy paymentProxy;
    private final ShipmentProxy shipmentProxy;

    @PostMapping(value = {"/", ""}, produces = "application/json")
    @CircuitBreaker(name = "orderDetails", fallbackMethod = "fallback")
    @Retry(name = "core-service")
    ResponseEntity<OrderDetails> checkout(@RequestBody BucketDetails bucketDetails) {
        UUID transId = UUID.randomUUID();
        log.info("Get order info for transaction id: {}", transId);
        OrderInfo orderInfo = coreProxy.getOrderInfo(bucketDetails, transId);
        log.info("Get payment info and create payment for transaction id: {}", transId);
        List<PaymentMethodDto> paymentInfo = paymentProxy.getPaymentInfo(transId);
        log.info("Get shipment info and create shipment for transaction id: {}", transId);
        List<ShipmentMethodDto> shipmentInfo = shipmentProxy.getShipmentInfo(transId);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(Objects.requireNonNull(orderInfo).getId());
        orderDetails.setTransId(transId);
        orderDetails.setProducts(orderInfo.getProducts());
        orderDetails.setPayments(paymentInfo);
        orderDetails.setShipments(shipmentInfo);

        return ResponseEntity.ok(orderDetails);
    }

    public ResponseEntity<?> fallback(BucketDetails bucketDto, RuntimeException runtimeException) {
        return new ResponseEntity<>("Something went wrong, please try again later",
                HttpStatus.GATEWAY_TIMEOUT);
    }
}
