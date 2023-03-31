package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.dto.AddressDto;
import com.amdaris.mentoring.core.dto.converter.AddressConverter;
import com.amdaris.mentoring.core.model.Address;
import com.amdaris.mentoring.core.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class AddressControllerTestsIT {
    @Autowired
    private AddressRepository addressRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that endpoint return all addresses")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        Address secondAddress = new Address();
        secondAddress.setCountry("Romania");
        secondAddress.setCity("Buharest");
        secondAddress.setStreet("Stefan Cel Mare str.");
        secondAddress.setHouse("11");
        secondAddress.setBlock("3");

        List<Address> addresses = List.of(firstAddress, secondAddress);

        addressRepository.saveAll(addresses);

        mvc.perform(MockMvcRequestBuilders.get("/address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(
                                addresses.stream()
                                        .map(address -> AddressConverter.toAddressDto.apply(address))
                                        .collect(Collectors.toList())
                        ),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return address by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        Address address = addressRepository.save(firstAddress);

        mvc.perform(MockMvcRequestBuilders.get("/address/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(
                                AddressConverter.toAddressDto.apply(firstAddress)
                        ),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists address by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        addressRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/address/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Address with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint return address by id")
    @Test
    public void findByFilter_dataIsPresent_returnExistingData() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        Address address = addressRepository.save(firstAddress);

        mvc.perform(MockMvcRequestBuilders.get("/address/part/" + address.getCountry())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(
                                List.of(AddressConverter.toAddressDto.apply(address))
                        ),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists address by filter")
    @Test
    public void findByFilter_dataNoPresent_returnErrorMessage() throws Exception {
        addressRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/address/part/Cahul")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Address - Cahul, doesn't found!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new address")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstAddress)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();

        List<Address> byCountryAndCityAndStreetAndHouseAndBlock =
                addressRepository.findByFilter(firstAddress.getCountry() + " " + firstAddress.getCity() + " " +
                        firstAddress.getStreet() + " " + firstAddress.getHouse() + " " + firstAddress.getBlock());

        Assertions.assertEquals(objectMapper.writeValueAsString(
                        AddressConverter.toAddressDto.apply(byCountryAndCityAndStreetAndHouseAndBlock.get(0))
                ),
                mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Test that exception throws when try to create address which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressRepository.save(firstAddress);

        mvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstAddress)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Address - AddressDto(id=1, country=Moldova, city=Chisinau, street=31 August str., block=A, house=21, userId=0), already exists",
                        result.getResolvedException().getMessage()));
    }
    @DisplayName("Test that address was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        Address address = addressRepository.save(firstAddress);

        firstAddress.setCity("Balti");
        AddressDto addressDto = AddressConverter.toAddressDto.apply(firstAddress);

        mvc.perform(MockMvcRequestBuilders.patch("/address/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(addressDto), result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update address which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        mvc.perform(MockMvcRequestBuilders.patch("/address/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstAddress)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Address with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted address by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        addressRepository.deleteAll();

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        Address address = addressRepository.save(firstAddress);

        mvc.perform(MockMvcRequestBuilders.delete("/address/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Address with id - " + address.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete address which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        addressRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/address/" + (short) 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Address with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }
}