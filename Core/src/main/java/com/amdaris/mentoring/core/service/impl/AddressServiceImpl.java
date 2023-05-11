package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.dto.AddressDto;
import com.amdaris.mentoring.core.dto.converter.AddressConverter;
import com.amdaris.mentoring.core.model.Address;
import com.amdaris.mentoring.core.repository.AddressRepository;
import com.amdaris.mentoring.core.service.AddressService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(address -> AddressConverter.toAddressDto.apply(address))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto findById(long id) {
        Optional<Address> address = addressRepository.findById(id);

        if (address.isPresent()) {
            return AddressConverter.toAddressDto.apply(address.get());
        }
        throw new NoSuchElementException("Address with id - " + id + " doesn't exist!");
    }

    @Override
    public List<AddressDto> findByPartOfAddress(String filter) {
        List<Address> findByAddress = addressRepository.findByFilter(filter);
        if (!findByAddress.isEmpty()) {
            return findByAddress.stream()
                    .map(address -> AddressConverter.toAddressDto.apply(address))
                    .collect(Collectors.toList());
        }

        throw new NoSuchElementException("Address - " + filter + ", doesn't found!");
    }

    @Override
    public AddressDto save(AddressDto addressDto) {
        List<Address> byFilter = addressRepository.findByFilter(addressDto.getCountry() + " " +
                addressDto.getCity() + " " + addressDto.getStreet() + " " +
                addressDto.getHouse() + " " + addressDto.getBlock());

        if (byFilter.size() > 0) {
            throw new EntityExistsException("Address - " + addressDto + ", already exists");
        }

        Address address = AddressConverter.toAddress.apply(addressDto);
        return AddressConverter.toAddressDto.apply(addressRepository.save(address));
    }

    @Override
    public AddressDto update(AddressDto addressDto, long id) {
        Optional<Address> findById = addressRepository.findById(id);
        Address address = AddressConverter.toAddress.apply(addressDto);

        if (findById.isPresent()) {
            address.setId(findById.get().getId());
            return AddressConverter.toAddressDto.apply(addressRepository.save(address));
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
