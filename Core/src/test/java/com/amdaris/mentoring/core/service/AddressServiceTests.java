package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.AddressDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

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

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        AddressDto secondAddress = new AddressDto();
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

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        AddressDto address = addressService.findByPartOfAddress(firstAddress.getCountry() + " " + firstAddress.getCity() + " " +
                firstAddress.getStreet() + " " + firstAddress.getHouse() + " " + firstAddress.getBlock()).get(0);

        Assertions.assertEquals(firstAddress.getCity(), address.getCity());
        Assertions.assertEquals(firstAddress.getCountry(), address.getCountry());
        Assertions.assertEquals(firstAddress.getStreet(), address.getStreet());
    }

    @DisplayName("Test that found by address throws error when didn't found any item")
    @Test
    public void findByAddress_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.findByPartOfAddress("Moldova Chisinau Valea Crucii 1 C"));

        Assertions.assertEquals("Address - Moldova Chisinau Valea Crucii 1 C, doesn't found!",
                exception.getMessage());
    }

    @DisplayName("Test that found by id address from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, addressService.findAll().size());

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        long addressId = addressService.findByPartOfAddress(firstAddress.getCountry() + " " + firstAddress.getCity() + " " +
                        firstAddress.getStreet() + " " + firstAddress.getHouse() + " " + firstAddress.getBlock())
                .get(0)
                .getId();

        AddressDto address = addressService.findById(addressId);

        Assertions.assertEquals(firstAddress.getCity(), address.getCity());
        Assertions.assertEquals(firstAddress.getCountry(), address.getCountry());
        Assertions.assertEquals(firstAddress.getStreet(), address.getStreet());
    }

    @DisplayName("Test that found by id address throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.findById(1L));

        Assertions.assertEquals("Address with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by filter addresses from database")
    @Test
    public void findByFilter_dataIsPresent_returnExistingData() {
        Assertions.assertEquals(0, addressService.findAll().size());

        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        AddressDto firstSavedAddress = addressService.save(firstAddress);

        AddressDto secondAddress = new AddressDto();
        secondAddress.setCountry("Moldova");
        secondAddress.setCity("Balti");
        secondAddress.setStreet("31 August str.");
        secondAddress.setHouse("21");
        secondAddress.setBlock("B");

        AddressDto secondSavedAddress = addressService.save(secondAddress);

        List<AddressDto> filterByCountry = addressService.findByPartOfAddress(firstAddress.getCountry());

        Assertions.assertEquals(2, filterByCountry.size());
        Assertions.assertEquals(List.of(firstSavedAddress, secondSavedAddress), filterByCountry);

        List<AddressDto> filterByCity = addressService.findByPartOfAddress(firstAddress.getCity());

        Assertions.assertEquals(1, filterByCity.size());
        Assertions.assertEquals(List.of(firstSavedAddress), filterByCity);

        List<AddressDto> filterByStreet = addressService.findByPartOfAddress(firstAddress.getStreet());

        Assertions.assertEquals(2, filterByStreet.size());
        Assertions.assertEquals(List.of(firstSavedAddress, secondSavedAddress), filterByStreet);

        List<AddressDto> filterByHouse = addressService.findByPartOfAddress(firstAddress.getHouse());

        Assertions.assertEquals(2, filterByHouse.size());
        Assertions.assertEquals(List.of(firstSavedAddress, secondSavedAddress), filterByHouse);

        List<AddressDto> filterByBlock = addressService.findByPartOfAddress(secondAddress.getBlock());

        Assertions.assertEquals(1, filterByBlock.size());
        Assertions.assertEquals(List.of(secondSavedAddress), filterByBlock);
    }

    @DisplayName("Test that found by filter addresses throws error when didn't found any item")
    @Test
    public void findByFilter_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.findByPartOfAddress("Cahul"));

        Assertions.assertEquals("Address - Cahul, doesn't found!", exception.getMessage());
    }

    @DisplayName("Test that address was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        List<AddressDto> addresses = addressService.findAll();

        Assertions.assertEquals(1, addresses.size());
        Assertions.assertEquals(firstAddress.getCity(), addresses.get(0).getCity());
        Assertions.assertEquals(firstAddress.getCountry(), addresses.get(0).getCountry());
        Assertions.assertEquals(firstAddress.getStreet(), addresses.get(0).getStreet());
    }

    @DisplayName("Test that save address throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> addressService.save(firstAddress));

        Assertions.assertEquals("Address - AddressDto(id=null, country=Moldova, city=Chisinau, street=31 August str., block=A, house=21, userId=0), already exists", exception.getMessage());
    }

    @DisplayName("Test that address was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        long addressId = addressService.findByPartOfAddress(firstAddress.getCountry() + " " + firstAddress.getCity() + " " +
                        firstAddress.getStreet() + " " + firstAddress.getHouse() + " " + firstAddress.getBlock())
                .get(0)
                .getId();

        firstAddress.setCity("Balti");
        addressService.update(firstAddress, addressId);

        AddressDto updatedAddress = addressService.findById(addressId);

        Assertions.assertEquals("Balti", updatedAddress.getCity());
    }

    @DisplayName("Test that update address throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> addressService.update(firstAddress, 1L));

        Assertions.assertEquals("Address with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that address was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("31 August str.");
        firstAddress.setHouse("21");
        firstAddress.setBlock("A");

        addressService.save(firstAddress);

        Assertions.assertEquals(1, addressService.findAll().size());

        long addressId = addressService.findByPartOfAddress(firstAddress.getCountry() + " " + firstAddress.getCity() + " " +
                        firstAddress.getStreet() + " " + firstAddress.getHouse() + " " + firstAddress.getBlock())
                .get(0)
                .getId();
        addressService.deleteById(addressId);

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
