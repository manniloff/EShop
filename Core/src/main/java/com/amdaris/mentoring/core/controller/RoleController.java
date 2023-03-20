package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> findAll() {
        log.info("Try to get list of roles");
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> findById(@PathVariable short id) {
        log.info("Try to get role by id - {}", id);
        return ResponseEntity.ok(roleService.findById(id));
    }

    @GetMapping(value = "/type/{role}", produces = "application/json")
    ResponseEntity<?> findByRole(@PathVariable String role) {
        log.info("Try to get role - {}", role);
        return ResponseEntity.ok(roleService.findByRole(role));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody Role role) {
        log.info("Try to save new role - {}", role.getRoleType());
        short roleId = roleService.save(role);
        if (roleId != 0) {
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody Role role, @PathVariable short id) {
        log.info("Try to update role - {}", role.getRoleType());
        return new ResponseEntity<>(roleService.update(role, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable short id) {
        log.info("Try to delete role with id - {}", id);
        roleService.deleteById(id);
        return ResponseEntity.ok("Role with id - " + id + ", was deleted");
    }
}
