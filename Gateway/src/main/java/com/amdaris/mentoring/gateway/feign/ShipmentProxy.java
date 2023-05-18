package com.amdaris.mentoring.gateway.feign;

import com.amdaris.mentoring.common.dto.ShipmentMethodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shipment-service")
public interface ShipmentProxy {
    @GetMapping(value = "/shipment/method/checkout/{transId}", produces = "application/json", consumes = "application/json")
    List<ShipmentMethodDto> getShipmentInfo(@PathVariable UUID transId);
}
