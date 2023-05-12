package com.amdaris.mentoring.gateway.feign;

import com.amdaris.mentoring.common.dto.BucketDto;
import com.amdaris.mentoring.common.model.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "core-service")
public interface CoreProxy {
    @PostMapping("/core/order/{transId}")
    ResponseEntity<OrderInfo> getOrderInfo(@RequestBody BucketDto bucketDto, @PathVariable UUID transId);
}
