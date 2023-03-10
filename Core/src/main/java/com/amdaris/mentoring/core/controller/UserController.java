package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    @GetMapping(value = "/lastname/{lastName}", produces = "application/json")
    ResponseEntity<?> findByLastName(@PathVariable String lastName) {
        log.info("Try to get user with lastName - {}", lastName);
        return ResponseEntity.ok(userService.findLastName(lastName));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody User user) {
        log.info("Try to save new user with phone number - {}", user.getPhoneNumber());
        long userId = userService.save(user);
        if (userId != 0) {
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody User user, @PathVariable long id) {
        log.info("Try to update user with phone number - {}", user.getPhoneNumber());
        long userId = userService.update(user, id);
        if (userId != 0) {
            return new ResponseEntity<>("Updated", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Not Modified", HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete user with phone number - {}", id);
        userService.deleteById(id);
        return ResponseEntity.ok("User with id - " + id + ", was deleted");
    }
}
