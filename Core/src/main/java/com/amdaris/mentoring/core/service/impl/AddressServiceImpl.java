package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.model.Address;
import com.amdaris.mentoring.core.repository.AddressRepository;
import com.amdaris.mentoring.core.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> findById(long id) {
        Optional<Address> address = addressRepository.findById(id);

        if (address.isPresent()) {
            return address;
        }
        throw new NoSuchElementException("Address with id - " + id + " doesn't exist!");
    }

    @Override
    public Optional<Address> findByAddress(String country, String city, String street, String house, String block) {
        Optional<Address> findByAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndBlock(
                country, city, street, house, block);

        if (findByAddress.isPresent()) {
            return findByAddress;
        }

        throw new NoSuchElementException("Address - " + country + " " + city + " " + street + " " + house +
                " " + block + ", doesn't exists!");
    }

    @Override
    public long save(Address address) {
        Optional<Address> findByAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndBlock(
                address.getCountry(), address.getCity(), address.getStreet(), address.getHouse(), address.getBlock());

        if (findByAddress.isPresent()) {
            throw new EntityExistsException("Address - " + address.getCountry() + " " + address.getCity() + " " +
                    address.getStreet() + " " + address.getHouse() + " " + address.getBlock() + ", exists!");
        }

        return addressRepository.save(address).getId();
    }

    @Override
    public Address update(Address address, long id) {
        Optional<Address> findById = addressRepository.findById(id);

        if (findById.isPresent()) {
            return addressRepository.save(address);
        }
        throw new NoSuchElementException("Address with id - " + id + " doesn't exist!");
    }

    @Override
    public long deleteById(long id) {
        Optional<Address> findById = addressRepository.findById(id);

        if (findById.isPresent()) {
            addressRepository.delete(findById.get());
            return findById.get().getId();
        }
        throw new NoSuchElementException("Address with id - " + id + " doesn't exist!");
    }

    @Override
    public void clear() {
        addressRepository.deleteAll();
    }
}
