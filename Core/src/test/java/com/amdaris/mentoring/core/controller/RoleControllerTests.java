package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.model.Address;
import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class RoleControllerTests {
    @Autowired
    private RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that endpoint return all roles")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        Role adminRole = new Role();
        adminRole.setRoleType("ADMIN");
        adminRole.setUsers(Set.of());

        List<Role> roles = List.of(customerRole, adminRole);

        roleRepository.saveAll(roles);

        mvc.perform(MockMvcRequestBuilders.get("/role")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(roles),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return role by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        Role role = roleRepository.save(customerRole);

        mvc.perform(MockMvcRequestBuilders.get("/role/" + role.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(customerRole),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists role by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        roleRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/role/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Role with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint return role by role type")
    @Test
    public void findByRole_dataIsPresent_returnExistingData() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        Role role = roleRepository.save(customerRole);

        mvc.perform(MockMvcRequestBuilders.get("/role/type/" + role.getRoleType())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(customerRole),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists role by role type")
    @Test
    public void findByRole_dataNoPresent_returnErrorMessage() throws Exception {
        roleRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/role/type/ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Role - ADMIN doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new role")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        mvc.perform(MockMvcRequestBuilders.post("/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRole)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Created", result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to create role which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        roleRepository.save(customerRole);

        mvc.perform(MockMvcRequestBuilders.post("/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRole)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Role - CUSTOMER, exists!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that role was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        Role role = roleRepository.save(customerRole);

        customerRole.setRoleType("MODERATOR");
        mvc.perform(MockMvcRequestBuilders.put("/role/" + role.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRole)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Updated", result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update role which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        mvc.perform(MockMvcRequestBuilders.put("/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRole)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Role with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted role by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        roleRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");
        customerRole.setUsers(Set.of());

        Role role = roleRepository.save(customerRole);

        mvc.perform(MockMvcRequestBuilders.delete("/role/" + role.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Role with id - " + role.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete role which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        roleRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/role/" + (short) 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Role with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

}