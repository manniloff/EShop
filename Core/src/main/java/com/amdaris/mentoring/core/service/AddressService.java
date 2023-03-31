package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> findAll();

    AddressDto findById(long id);

    List<AddressDto> findByPartOfAddress(String filter);

    AddressDto save(AddressDto address);

    AddressDto update(AddressDto address, long id);

    long deleteById(long id);

    void clear();
}
