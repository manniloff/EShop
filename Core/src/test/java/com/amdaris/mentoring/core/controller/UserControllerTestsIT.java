package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.dto.UserDto;
import com.amdaris.mentoring.core.dto.converter.UserConverter;
import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
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
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class UserControllerTestsIT {
    @Autowired
    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("Test that endpoint return all users")
    @Test
    public void findAllByCriteria_dataIsPresent_returnAllData() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        User secondUser = new User();
        secondUser.setEmail("test2@user.md");
        secondUser.setRole(new Role());
        secondUser.setBirthday(LocalDate.of(1990, 10, 5));
        secondUser.setFirstName("Test2");
        secondUser.setLastName("User2");
        secondUser.setPhoneNumber("068456783");
        secondUser.setAddresses(Set.of());
        secondUser.setPassword("secretpass2");

        userRepository.saveAll(List.of(firtUser, secondUser));

        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(user -> UserConverter.toUserDto.apply(user))
                .collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(users),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return all users")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        User secondUser = new User();
        secondUser.setEmail("test2@user.md");
        secondUser.setRole(new Role());
        secondUser.setBirthday(LocalDate.of(1990, 10, 5));
        secondUser.setFirstName("Test2");
        secondUser.setLastName("User2");
        secondUser.setPhoneNumber("068456783");
        secondUser.setAddresses(Set.of());
        secondUser.setPassword("secretpass2");

        userRepository.saveAll(List.of(firtUser, secondUser));

        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(user -> UserConverter.toUserDto.apply(user))
                .collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(users),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return user by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = UserConverter.toUserDto.apply(userRepository.save(firtUser));

        mvc.perform(MockMvcRequestBuilders.get("/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(user),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists user by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("User with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint return user by email")
    @Test
    public void findByEmail_dataIsPresent_returnExistingData() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = UserConverter.toUserDto.apply(userRepository.save(firtUser));

        mvc.perform(MockMvcRequestBuilders.get("/user/email/" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(user),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists user by email")
    @Test
    public void findByEmail_dataNoPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/user/email/user@wrong.ru")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("User with email - user@wrong.ru doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint return user by phone number")
    @Test
    public void findByPhone_dataIsPresent_returnExistingData() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = UserConverter.toUserDto.apply(userRepository.save(firtUser));

        mvc.perform(MockMvcRequestBuilders.get("/user/phone/" + user.getPhoneNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(user),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists user by phone number")
    @Test
    public void findByPhone_dataNoPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/user/phone/068145587")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("User with phone - 068145587 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint return user by last name")
    @Test
    public void findByFullName_dataIsPresent_returnExistingData() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = UserConverter.toUserDto.apply(userRepository.save(firtUser));

        mvc.perform(MockMvcRequestBuilders.get("/user/fullName/" + user.getFirstName() + " " + user.getLastName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(List.of(user)),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists user by last name")
    @Test
    public void findByFullName_dataNoPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/user/fullName/John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("User with full name - John doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new user")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        userRepository.deleteAll();

        UserDto firtUser = new UserDto();
        firtUser.setEmail("test@user.md");
        firtUser.setRole("CUSTOMER");
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setPassword("secretpass");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firtUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        UserDto byEmail = UserConverter.toUserDto.apply(userRepository.findByEmail(firtUser.getEmail()).get());

        Assertions.assertEquals(objectMapper.writeValueAsString(byEmail), mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Test that exception throws when try to create user which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = UserConverter.toUserDto.apply(userRepository.save(firtUser));

        mvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("User with email - test@user.md, exists!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that user was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        UserDto user = UserConverter.toUserDto.apply(userRepository.save(firtUser));

        user.setEmail("test@amdaris.md");
        mvc.perform(MockMvcRequestBuilders.patch("/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(user), result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update user which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        mvc.perform(MockMvcRequestBuilders.patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserConverter.toUserDto.apply(firtUser))))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("User with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted user by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        userRepository.deleteAll();

        User firtUser = new User();
        firtUser.setEmail("test@user.md");
        firtUser.setRole(new Role());
        firtUser.setBirthday(LocalDate.of(1990, 11, 4));
        firtUser.setFirstName("Test");
        firtUser.setLastName("User");
        firtUser.setPhoneNumber("068456784");
        firtUser.setAddresses(Set.of());
        firtUser.setPassword("secretpass");

        User user = userRepository.save(firtUser);

        mvc.perform(MockMvcRequestBuilders.delete("/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("User with id - " + user.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete user which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        userRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/user/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("User with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }
}
