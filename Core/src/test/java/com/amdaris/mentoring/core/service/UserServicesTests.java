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

import javax.persistence.EntityExistsException;
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
    public void findAllByCriteria_dataIsPresent_returnAllData() {
        AddressDto firstAddress = new AddressDto();
        firstAddress.setCountry("Moldova");
        firstAddress.setCity("Chisinau");
        firstAddress.setStreet("Stefan cel Mare");
        firstAddress.setHouse("11");
        firstAddress.setBlock("B");

        AddressDto savedFirstAddress = addressService.save(firstAddress);

        AddressDto secondAddress = new AddressDto();
        secondAddress.setCountry("Moldova");
        secondAddress.setCity("Chisinau");
        secondAddress.setStreet("Puskin");
        secondAddress.setHouse("24");

        AddressDto savedSecondAddress = addressService.save(secondAddress);

        AddressDto thirdAddress = new AddressDto();
        thirdAddress.setCountry("Moldova");
        thirdAddress.setCity("Tiraspol");
        thirdAddress.setStreet("Crasnoi Armii");
        thirdAddress.setHouse("13");
        thirdAddress.setBlock("2");

        AddressDto savedThirdAddress = addressService.save(thirdAddress);

        AddressDto fourthAddress = new AddressDto();
        fourthAddress.setCountry("Moldova");
        fourthAddress.setCity("Balti");
        fourthAddress.setStreet("Independentii");
        fourthAddress.setHouse("18");

        AddressDto savedFourthAddress = addressService.save(fourthAddress);

        AddressDto fifthAddress = new AddressDto();
        fifthAddress.setCountry("Moldova");
        fifthAddress.setCity("Cahul");
        fifthAddress.setStreet("Eminescu");
        fifthAddress.setHouse("11");
        fifthAddress.setBlock("B");

        AddressDto savedFifthAddress = addressService.save(fifthAddress);

        UserDto firstUser = new UserDto();
        firstUser.setEmail("test@user.md");
        firstUser.setRole("CUSTOMER");
        firstUser.setBirthday(LocalDate.of(1990, 11, 4));
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        firstUser.setPhoneNumber("078456784");
        firstUser.setAddressIds(Set.of(savedFirstAddress.getId()));
        firstUser.setPassword("secretpass");

        UserDto saveFirstUser = userService.save(firstUser);

        UserDto secondUser = new UserDto();
        secondUser.setEmail("test2@user.md");
        secondUser.setRole("ADMIN");
        secondUser.setBirthday(LocalDate.of(1990, 10, 5));
        secondUser.setFirstName("Test2");
        secondUser.setLastName("User2");
        secondUser.setPhoneNumber("068456783");
        secondUser.setAddressIds(Set.of(savedSecondAddress.getId()));
        secondUser.setPassword("secretpass2");

        userService.save(secondUser);

        UserDto thirdUser = new UserDto();
        thirdUser.setEmail("test3@user.md");
        thirdUser.setRole("CUSTOMER");
        thirdUser.setBirthday(LocalDate.of(1993, 10, 5));
        thirdUser.setFirstName("Test3");
        thirdUser.setLastName("User3");
        thirdUser.setPhoneNumber("079456784");
        thirdUser.setAddressIds(Set.of(savedThirdAddress.getId()));
        thirdUser.setPassword("secretpass3");

        userService.save(thirdUser);

        UserDto fourthUser = new UserDto();
        fourthUser.setEmail("test4@user.md");
        fourthUser.setRole("MODERATOR");
        fourthUser.setBirthday(LocalDate.of(1980, 2, 15));
        fourthUser.setFirstName("Test4");
        fourthUser.setLastName("User4");
        fourthUser.setPhoneNumber("060456785");
        fourthUser.setAddressIds(Set.of(savedFourthAddress.getId()));
        fourthUser.setPassword("secretpass4");

        userService.save(fourthUser);


        UserDto fifthUser = new UserDto();
        fifthUser.setEmail("test5@user.md");
        fifthUser.setRole("ADMIN");
        fifthUser.setBirthday(LocalDate.of(1985, 7, 25));
        fifthUser.setFirstName("Test5");
        fifthUser.setLastName("User5");
        fifthUser.setPhoneNumber("061456786");
        fifthUser.setAddressIds(Set.of(savedFifthAddress.getId()));
        fifthUser.setPassword("secretpass5");

        userService.save(fifthUser);

        PageView pageView = new PageView();
        pageView.setPage(0);
        pageView.setPageSize(10);
        pageView.setSort("id");

        UserSearchCriteria userSearchCriteria = new UserSearchCriteria();

        Page<UserDto> userListWithoutCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(5, userListWithoutCriteria.getTotalElements());

        userSearchCriteria.setAddresses(List.of(savedFirstAddress.getId(),
                savedFourthAddress.getId()));

        Page<UserDto> userListWithAddressesCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(2, userListWithAddressesCriteria.getTotalElements());

        userSearchCriteria.setAddresses(null);
        userSearchCriteria.setEmail("test");

        Page<UserDto> userListWithEmailCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(5, userListWithEmailCriteria.getTotalElements());

        userSearchCriteria.setEmail(null);
        userSearchCriteria.setLastName("User3");

        Page<UserDto> userListWithLastNameCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(1, userListWithLastNameCriteria.getTotalElements());

        userSearchCriteria.setLastName(null);
        userSearchCriteria.setFirstName("Test");

        Page<UserDto> userListWithFirstNameCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(5, userListWithFirstNameCriteria.getTotalElements());

        userSearchCriteria.setFirstName(null);
        userSearchCriteria.setEndPeriod(LocalDate.of(1985, 7, 25));

        Page<UserDto> userListWithEndaDateCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(2, userListWithEndaDateCriteria.getTotalElements());

        userSearchCriteria.setStartPeriod(LocalDate.of(1990, 1, 1));
        userSearchCriteria.setEndPeriod(LocalDate.of(1995, 7, 25));

        Page<UserDto> userListBetweenDatesCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(3, userListBetweenDatesCriteria.getTotalElements());

        userSearchCriteria.setStartPeriod(LocalDate.of(1900, 1, 1));
        userSearchCriteria.setRole("CUSTOMER");

        Page<UserDto> userListWithRoleCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(2, userListWithRoleCriteria.getTotalElements());

        userSearchCriteria.setRole(null);
        userSearchCriteria.setPhoneNumber("06");

        Page<UserDto> userListWithPhoneNumberCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(3, userListWithPhoneNumberCriteria.getTotalElements());

        userSearchCriteria.setRole("ADMIN");
        userSearchCriteria.setFirstName("Test2");
        userSearchCriteria.setLastName("User");
        userSearchCriteria.setEmail("@user");

        Page<UserDto> userListWithCriteria = userService.findByCriteria(pageView, userSearchCriteria);
        Assertions.assertEquals(1, userListWithCriteria.getTotalElements());
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

    @DisplayName("Test that found user by id from database")
    @Test
    public void findByFullName_dataIsPresent_returnExistingData() {
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

        UserDto user = userService.findByFullName(firstUser.getFirstName() + " " + firstUser.getLastName()).get(0);

        Assertions.assertEquals(firstUser.getEmail(), user.getEmail());
        Assertions.assertEquals(firstUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(firstUser.getLastName(), user.getLastName());
        Assertions.assertEquals(firstUser.getPhoneNumber(), user.getPhoneNumber());
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
