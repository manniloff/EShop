package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class AddressServiceTests {

    @Autowired
    private AddressService addressService;

    @BeforeEach
    public void beforeEach() {
        addressService.clear();
    }

    @DisplayName("Test that all addresses were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, addressService.findAll().size());

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        Address secondAddress = new Address();
        secondAddress.setCountry("Romania");
        secondAddress.setCity("Buharest");
        secondAddress.setStreet("Stefan Cel Mare str.");
        secondAddress.setHouse("11");
        secondAddress.setBlock("3");

        addressService.save(secondAddress);

        Assertions.assertEquals(2, addressService.findAll().size());
    }

    @DisplayName("Test that found by address from database")
    @Test
    public void findByAddress_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, addressService.findAll().size());

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        Optional<Address> address = addressService.findByAddress(firstAddress.getCountry(), firstAddress.getCity(), firstAddress.getStreet(),
                firstAddress.getHouse(), firstAddress.getBlock());

        Assertions.assertEquals(firstAddress, address.get());
    }

    @DisplayName("Test that found by address throws error when didn't found any item")
    @Test
    public void findByAddress_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.findByAddress("Moldova", "Chisinau", "Valea Crucii", "1", "C"));

        Assertions.assertEquals("Address - Moldova Chisinau Valea Crucii 1 C, doesn't exists!",
                exception.getMessage());
    }

    @DisplayName("Test that found by id address from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, addressService.findAll().size());

        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");
        firstAddress.setUser(null);

        addressService.save(firstAddress);

        long addressId = addressService.findByAddress(firstAddress.getCountry(), firstAddress.getCity(), firstAddress.getStreet(),
                firstAddress.getHouse(), firstAddress.getBlock()).get().getId();

        Optional<Address> address = addressService.findById(addressId);

        Assertions.assertEquals(firstAddress, address.get());
    }

    @DisplayName("Test that found by id address throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.findById(1L));

        Assertions.assertEquals("Address with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that address was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");
        firstAddress.setUser(null);

        addressService.save(firstAddress);

        List<Address> categories = addressService.findAll();

        Assertions.assertEquals(1, categories.size());
        Assertions.assertEquals("Chisinau", categories.get(0).getCity());
        Assertions.assertEquals(firstAddress, categories.get(0));
    }

    @DisplayName("Test that save address throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> addressService.save(firstAddress));

        Assertions.assertEquals("Address - Moldova Chisinau 31 August str. 21 A, exists!", exception.getMessage());
    }

    @DisplayName("Test that address was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        long addressId = addressService.findByAddress(firstAddress.getCountry(), firstAddress.getCity(), firstAddress.getStreet(),
                firstAddress.getHouse(), firstAddress.getBlock()).get().getId();

        firstAddress.setCity("Balti");
        addressService.update(firstAddress, addressId);

        Optional<Address> updatedAddress = addressService.findById(addressId);

        Assertions.assertEquals("Balti", updatedAddress.get().getCity());
    }

    @DisplayName("Test that update address throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");
        firstAddress.setUser(null);

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.update(firstAddress, 1L));

        Assertions.assertEquals("Address with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that address was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        Address firstAddress = new Address();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");
        firstAddress.setUser(null);

        addressService.save(firstAddress);

        List<Address> addresses = addressService.findAll();
        Assertions.assertEquals(1, addresses.size());
        addressService.deleteById(addresses.get(0).getId());

        Assertions.assertEquals(0, addressService.findAll().size());
    }

    @DisplayName("Test that delete address throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.deleteById(1L));

        Assertions.assertEquals("Address with id - 1 doesn't exist!", exception.getMessage());
    }
}
