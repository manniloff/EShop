package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class RoleServiceTests {
    @Autowired
    private RoleService roleService;

    @BeforeEach
    public void beforeEach() {
        roleService.clear();
    }

    @DisplayName("Test that all roles were returned")
    @Test
    public void findAll_dataIsPresent_returnAllData() {
        Assertions.assertEquals(0, roleService.findAll().size());

        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        roleService.save(customerRole);

        Role adminRole = new Role();
        adminRole.setRoleType("ADMIN");

        roleService.save(adminRole);

        Assertions.assertEquals(2, roleService.findAll().size());
    }

    @DisplayName("Test that found by role from database")
    @Test
    public void findByRole_dataIsPresent_returnExistingData() {
        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        roleService.save(customerRole);

        Optional<Role> foundByRole = roleService.findByRole(customerRole.getRoleType());

        Assertions.assertEquals(customerRole.getId(), foundByRole.get().getId());
        Assertions.assertEquals(customerRole.getRoleType(), foundByRole.get().getRoleType());
    }

    @DisplayName("Test that found by role throws error when didn't found any item")
    @Test
    public void findByRole_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> roleService.findByRole("USER"));

        Assertions.assertEquals("Role - USER doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by id role from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        Role cusomerRole = new Role();
        cusomerRole.setRoleType("CUSTOMER");

        roleService.save(cusomerRole);

        Optional<Role> foundByRole = roleService.findByRole("CUSTOMER");
        Optional<Role> role = roleService.findById((foundByRole.get().getId()));

        Assertions.assertEquals(cusomerRole.getRoleType(), role.get().getRoleType());
    }

    @DisplayName("Test that found by id role throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> roleService.findById((short) 1));

        Assertions.assertEquals("Role with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that role was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        roleService.save(customerRole);

        List<Role> roles = roleService.findAll();

        Assertions.assertEquals(1, roles.size());
        Assertions.assertEquals("CUSTOMER", roles.get(0).getRoleType());
    }

    @DisplayName("Test that save role throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        roleService.save(customerRole);

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class, () -> roleService.save(customerRole));

        Assertions.assertEquals("Role - CUSTOMER, exists!", exception.getMessage());
    }

    @DisplayName("Test that role was updated in database")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() {
        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        roleService.save(customerRole);

        short roleId = roleService.findByRole(customerRole.getRoleType()).get().getId();

        customerRole.setRoleType("MODERATOR");
        roleService.update(customerRole, roleId);

        Optional<Role> updatedRole = roleService.findById(roleId);

        Assertions.assertEquals("MODERATOR", updatedRole.get().getRoleType());
    }

    @DisplayName("Test that update role throws error when didn't found any item")
    @Test
    public void update_dataNoPresent_returnErrorMessage() {
        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> roleService.update(customerRole, (short) 1));

        Assertions.assertEquals("Role with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that role was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        Role customerRole = new Role();
        customerRole.setRoleType("CUSTOMER");

        roleService.save(customerRole);

        List<Role> roles = roleService.findAll();
        Assertions.assertEquals(1, roles.size());
        roleService.deleteById(roles.get(0).getId());

        Assertions.assertEquals(0, roleService.findAll().size());
    }

    @DisplayName("Test that delete role throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> roleService.deleteById((short) 1));

        Assertions.assertEquals("Role with id - 1 doesn't exist!", exception.getMessage());
    }
}
