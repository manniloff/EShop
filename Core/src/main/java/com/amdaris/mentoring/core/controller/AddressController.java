package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.dto.AddressDto;
import com.amdaris.mentoring.core.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> findAll() {
        log.info("Try to get list of addresses");
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> findById(@PathVariable long id) {
        log.info("Try to get address by id - {}", id);
        return ResponseEntity.ok(addressService.findById(id));
    }

    @GetMapping(value = "/part/{filter}", produces = "application/json")
    ResponseEntity<?> findByPartOfAddress(@PathVariable String filter) {
        log.info("Try to get address by filter - {}", filter);
        return ResponseEntity.ok(addressService.findByPartOfAddress(filter));
    }

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody AddressDto addressDto) {
        log.info("Try to save new address with id - {}", addressDto);
        return new ResponseEntity<>(addressService.save(addressDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody AddressDto addressDto, @PathVariable long id) {
        log.info("Try to update address with id- {}", id);
        return new ResponseEntity<>(addressService.update(addressDto, id), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete address with id - {}", id);
        addressService.deleteById(id);
        return ResponseEntity.ok("Address with id - " + id + ", was deleted");
    }
}
