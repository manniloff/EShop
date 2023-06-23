package com.amdaris.mentoring.common.Application.shipment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amdaris.mentoring.common.dto.ShipmentMethodDto;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shipment/method")
public class ShipmentMethodController {
    @GetMapping(value = "/checkout/{transId}", produces = "application/json", consumes = "application/json")
    public List<ShipmentMethodDto> getShipmentInfo() {
        log.info("Getting shipment method list");
        return List.of(ShipmentMethodDto.builder().house("house")
                        .apartmentNumber("apartmentNumber")
                        .building("building")
                        .city("city")
                        .country("country")
                        .street("street")
                        .title("DHL")
                .build(),
                ShipmentMethodDto.builder()
                        .house("house")
                        .apartmentNumber("apartmentNumber")
                        .building("building")
                        .city("city")
                        .country("country")
                        .street("street")
                        .title("UPS")
                        .build());
    }
}
