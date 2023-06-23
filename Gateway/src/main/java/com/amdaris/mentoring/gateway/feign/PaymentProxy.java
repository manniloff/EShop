package com.amdaris.mentoring.gateway.feign;

import com.amdaris.mentoring.common.dto.PaymentMethodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "payment-service")
public interface PaymentProxy {
    @GetMapping(value = "/payment/method/checkout/{transId}", produces = "application/json", consumes = "application/json")
    List<PaymentMethodDto> getPaymentInfo(@PathVariable UUID transId);
}
