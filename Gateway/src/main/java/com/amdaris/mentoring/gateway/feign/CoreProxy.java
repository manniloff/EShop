package com.amdaris.mentoring.gateway.feign;

import com.amdaris.mentoring.common.model.BucketDetails;
import com.amdaris.mentoring.common.model.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "core-service")
public interface CoreProxy {
    @PostMapping(value = "/core/order/checkout/{transId}", produces = "application/json", consumes = "application/json")
    OrderInfo getOrderInfo(@RequestBody BucketDetails bucketDetails, @PathVariable UUID transId);
}
