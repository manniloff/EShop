package com.amdaris.mentoring.core.dto.converter;

import com.amdaris.mentoring.core.dto.AddressDto;
import com.amdaris.mentoring.core.model.Address;

import java.util.Optional;
import java.util.function.Function;

public class AddressConverter {
    public static Function<Address, AddressDto> toAddressDto =
            address -> AddressDto.builder()
                    .id(address.getId())
                    .country(address.getCountry())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .house(address.getHouse())
                    .block(address.getBlock())
                    .userId(address.getId())
                    .build();

    public static Function<AddressDto, Address> toAddress =
            addressDto -> Address.builder()
                    .id(Optional.ofNullable(addressDto.getId()).orElse(0L))
                    .country(addressDto.getCountry())
                    .city(addressDto.getCity())
                    .street(addressDto.getStreet())
                    .house(addressDto.getHouse())
                    .block(addressDto.getBlock())
                    .build();
}
