package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.AddressDto;
import com.amdaris.mentoring.core.dto.UserDto;
import com.amdaris.mentoring.core.dto.criteria.UserSearchCriteria;
import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.util.PageView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import jakarta.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
public class UserServicesTests {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @BeforeEach
    public void beforeEach() {
        userService.clear();
        addressService.clear();
    }

    @DisplayName("Test that all users were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, userService.findAll().size());

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("068456784");
        firstUser.setAddressIds(Set.of());
        firstUser.setPassword("secretpass");

        userService.save(firstUser);

        UserDto secondUser = new UserDto();
        secondUser.setEmail("test2@user.md");
        secondUser.setRole("ADMIN");
        secondUser.setBirthday(LocalDate.of(1990, 10, 5));
        secondUser.setFirstName("Test2");
        secondUser.setLastName("User2");
        secondUser.setPhoneNumber("068456783");
        secondUser.setAddressIds(Set.of());
        secondUser.setPassword("secretpass2");

        userService.save(secondUser);

        Assertions.assertEquals(2, userService.findAll().size());
    }

    @DisplayName("Test that found user by email from database")
    @Test
    public void findByEmail_dataIsPresent_returnExistingData() {
        UserDto firstUser = new UserDto();
        firstUser.setEmail("test@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("068456784");
        firstUser.setAddressIds(Set.of());
        firstUser.setPassword("secretpass");

        userService.save(firstUser);

        UserDto foundByEmail = userService.findByEmail(firstUser.getEmail());

        Assertions.assertEquals(firstUser.getEmail(), foundByEmail.getEmail());
        Assertions.assertEquals(firstUser.getFirstName(), foundByEmail.getFirstName());
        Assertions.assertEquals(firstUser.getLastName(), foundByEmail.getLastName());
        Assertions.assertEquals(firstUser.getPhoneNumber(), foundByEmail.getPhoneNumber());
    }

    @DisplayName("Test that found user by email throws error when didn't found any item")
    @Test
    public void findByEmail_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("test@user.md"));

        Assertions.assertEquals("User with email - test@user.md doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found user by id from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        UserDto firstUser = new UserDto();
        firstUser.setEmail("test99@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test99");
        firstUser.setLastName("User99");
        firstUser.setPhoneNumber("068456799");
        firstUser.setAddressIds(Set.of());
        firstUser.setPassword("secretpass99");

        userService.save(firstUser);

        UserDto user = userService.findByEmail("test99@user.md");

        Assertions.assertEquals(firstUser.getEmail(), user.getEmail());
        Assertions.assertEquals(firstUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(firstUser.getLastName(), user.getLastName());
        Assertions.assertEquals(firstUser.getPhoneNumber(), user.getPhoneNumber());
    }

    @DisplayName("Test that found user by id throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(1L));

        Assertions.assertEquals("User with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found user by id throws error when didn't found any item")
    @Test
    public void findByFullName_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByFullName("Test User"));

        Assertions.assertEquals("User with full name - Test User doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that user was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        UserDto firtUser = new UserDto();
        firtUser.setEmail("test@user.md");
        firtUser.setRole("CUSTOMER");
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddressIds(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        List<UserDto> users = userService.findAll();

        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("test@user.md", users.get(0).getEmail());
    }

    @DisplayName("Test that save user throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        UserDto firstUser = new UserDto();
        firstUser.setEmail("testUser@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("068456784");
        firstUser.setAddressIds(Set.of());
        firstUser.setPassword("secretpass");

        userService.save(firstUser);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> userService.save(firstUser));

        Assertions.assertEquals("User with email - testUser@user.md, exists!", exception.getMessage());
    }

    @DisplayName("Test that user was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        UserDto firtUser = new UserDto();
        firtUser.setEmail("test@user.md");
        firtUser.setRole("CUSTOMER");
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddressIds(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = userService.save(firtUser);

        firtUser.setEmail("test@amdaris.md");
        UserDto updatedUser = userService.update(firtUser, user.getId());

        Assertions.assertEquals("test@amdaris.md", updatedUser.getEmail());
    }

    @DisplayName("Test that update user throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        UserDto firtUser = new UserDto();
        firtUser.setEmail("test@user.md");
        firtUser.setRole("CUSTOMER");
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddressIds(Set.of());
        firtUser.setPassword("secretpass");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.update(firtUser, 1L));

        Assertions.assertEquals("User with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that user was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        UserDto firtUser = new UserDto();
        firtUser.setEmail("test@user.md");
        firtUser.setRole("CUSTOMER");
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddressIds(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        List<UserDto> users = userService.findAll();
        Assertions.assertEquals(1, users.size());
        userService.deleteById(users.get(0).getId());

        Assertions.assertEquals(0, userService.findAll().size());
    }

    @DisplayName("Test that delete user throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.deleteById(1L));

        Assertions.assertEquals("User with id - 1 doesn't exist!", exception.getMessage());
    }
}
