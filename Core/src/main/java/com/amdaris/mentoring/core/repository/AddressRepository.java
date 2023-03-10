package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCountryAndCityAndStreetAndHouseAndBlock(String country, String city, String street,
                                                                    String house, String block);
}
