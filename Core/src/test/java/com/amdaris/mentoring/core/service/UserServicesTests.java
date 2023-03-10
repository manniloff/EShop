package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServicesTests {
    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        userService.clear();
    }

    @DisplayName("Test that all users were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, userService.findAll().size());

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        User secondUser = new User();
        secondUser.setEmail("test2@user.md");
        secondUser.setRole(new Role());
        secondUser.setBirthday(LocalDateTime.of(1990, 10, 5, 12, 30));
        secondUser.setFirstName("Test2");
        secondUser.setLastName("User2");
        secondUser.setPhoneNumber("068456783");
        secondUser.setAddresses(Set.of());
        secondUser.setPassword("secretpass2");

        userService.save(secondUser);

        Assertions.assertEquals(2, userService.findAll().size());
    }

    @DisplayName("Test that found user by email from database")
    @Test
    public void findByEmail_dataIsPresent_returnExistingData() {
        User firstUser = new User();
        firstUser.setEmail("test@user.md");
        firstUser.setRole(new Role());
        firstUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("068456784");
        firstUser.setAddresses(Set.of());
        firstUser.setPassword("secretpass");

        userService.save(firstUser);

        Optional<User> foundByEmail = userService.findByEmail(firstUser.getEmail());

        Assertions.assertEquals(firstUser.getEmail(), foundByEmail.get().getEmail());
        Assertions.assertEquals(firstUser.getFirstName(), foundByEmail.get().getFirstName());
        Assertions.assertEquals(firstUser.getLastName(), foundByEmail.get().getLastName());
        Assertions.assertEquals(firstUser.getPhoneNumber(), foundByEmail.get().getPhoneNumber());
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
        User firstUser = new User();
        firstUser.setEmail("test@user.md");
        firstUser.setRole(new Role());
        firstUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("068456784");
        firstUser.setAddresses(Set.of());
        firstUser.setPassword("secretpass");

        userService.save(firstUser);

        Optional<User> foundByEmail = userService.findByEmail("test@user.md");
        Optional<User> user = userService.findById((foundByEmail.get().getId()));

        Assertions.assertEquals(firstUser.getEmail(), user.get().getEmail());
        Assertions.assertEquals(firstUser.getFirstName(), user.get().getFirstName());
        Assertions.assertEquals(firstUser.getLastName(), user.get().getLastName());
        Assertions.assertEquals(firstUser.getPhoneNumber(), user.get().getPhoneNumber());
    }

    @DisplayName("Test that found user by id throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(1L));

        Assertions.assertEquals("User with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that гыук was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        List<User> users = userService.findAll();

        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("test@user.md", users.get(0).getEmail());
    }

    @DisplayName("Test that save user throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> userService.save(firtUser));

        Assertions.assertEquals("User with email - test@user.md, exists!", exception.getMessage());
    }

    @DisplayName("Test that user was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        long userId = userService.findByEmail(firtUser.getEmail()).get().getId();

        firtUser.setEmail("test@amdaris.md");
        userService.update(firtUser, userId);

        Optional<User> updatedUser = userService.findById(userId);

        Assertions.assertEquals("test@amdaris.md", updatedUser.get().getEmail());
    }

    @DisplayName("Test that update user throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.update(firtUser, 1L));

        Assertions.assertEquals("User with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that user was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDateTime.of(1990, 11, 4, 11, 30));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        userService.save(firtUser);

        List<User> users = userService.findAll();
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
