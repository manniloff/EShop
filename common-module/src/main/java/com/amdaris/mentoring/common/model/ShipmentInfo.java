package com.amdaris.mentoring.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentInfo {
    private String title;
    private String country;
    private String city;
    private String street;
    private String house;
    private String building;
    private String apartmentNumber;
}
