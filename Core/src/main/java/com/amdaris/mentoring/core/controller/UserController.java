package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.dto.UserDto;
import com.amdaris.mentoring.core.dto.criteria.UserSearchCriteria;
import com.amdaris.mentoring.core.service.AddressService;
import com.amdaris.mentoring.core.service.UserService;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/core/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AddressService addressService;

    @GetMapping(value = "/filter", produces = "application/json")
    ResponseEntity<?> findByCriteriaAll(PageView pageView, UserSearchCriteria criteria) {
        log.info("Try to get list of products by criteria");
        return ResponseEntity.ok(userService.findByCriteria(pageView, criteria));
    }

    @GetMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> findAll() {
        log.info("Try to get list of users");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> findById(@PathVariable long id) {
        log.info("Try to get user by id - {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping(value = "/email/{email}", produces = "application/json")
    ResponseEntity<?> findByEmail(@PathVariable String email) {
        log.info("Try to get user with email - {}", email);
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping(value = "/phone/{phoneNumber}", produces = "application/json")
    ResponseEntity<?> findByPhoneNumber(@PathVariable String phoneNumber) {
        log.info("Try to get user with phone - {}", phoneNumber);
        return ResponseEntity.ok(userService.findByPhoneNumber(phoneNumber));
    }

    @GetMapping(value = "/fullName/{fullName}", produces = "application/json")
    ResponseEntity<?> findByFullName(@PathVariable String fullName) {
        log.info("Try to get user with fullName - {}", fullName);
        return ResponseEntity.ok(userService.findByFullName(fullName));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody UserDto userDto) {
        log.info("Try to save new user with phone number - {}", userDto.getPhoneNumber());
        return new ResponseEntity<>(userService.save(userDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody UserDto userDto, @PathVariable long id) {
        log.info("Try to update user with phone number - {}", userDto.getPhoneNumber());
        return new ResponseEntity<>(userService.update(userDto, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete user with phone number - {}", id);
        userService.deleteById(id);
        return ResponseEntity.ok("User with id - " + id + ", was deleted");
    }
}
