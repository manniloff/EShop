package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<Address> findAll();

    Optional<Address> findById(long id);

    Optional<Address> findByAddress(String country, String city, String street, String house, String block);

    Address save(Address address);

    Address update(Address address, long id);

    long deleteById(long id);

    void clear();
}
