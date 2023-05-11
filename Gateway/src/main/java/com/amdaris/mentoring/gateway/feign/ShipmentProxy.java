package com.amdaris.mentoring.gateway.feign;

import com.amdaris.mentoring.common.model.ShipmentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shipment-service")
public interface ShipmentProxy {
    @GetMapping("/shipment/info/{transId}")
    ResponseEntity<List<ShipmentInfo>> getShipmentInfo(@PathVariable UUID transId);
}
