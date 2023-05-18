package com.amdaris.mentoring.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentMethodDto {
    private String title;
    private String country;
    private String city;
    private String street;
    private String house;
    private String building;
    private String apartmentNumber;
}
