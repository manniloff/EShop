package com.amdaris.mentoring.gateway.feign;

import com.amdaris.mentoring.common.model.PaymentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "payment-service")
public interface PaymentProxy {
    @GetMapping("/payment/{transId}")
    ResponseEntity<List<PaymentInfo>> getPaymentInfo(@PathVariable UUID transId);
}
