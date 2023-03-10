package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.model.Address;
import com.amdaris.mentoring.core.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @PostMapping(value = {"", "/"}, produces = "application/json")
    ResponseEntity<?> create(@RequestBody Address address) {
        log.info("Try to save new address with id - {}", address.getId());
        long addressId = addressService.save(address);
        if (addressId != 0) {
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> update(@RequestBody Address address, @PathVariable long id) {
        log.info("Try to update address with id- {}", address.getId());
        long addressId = addressService.update(address, id);
        if (addressId != 0) {
            return new ResponseEntity<>("Updated", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Not Modified", HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<?> deleteById(@PathVariable long id) {
        log.info("Try to delete address with id - {}", id);
        addressService.deleteById(id);
        return ResponseEntity.ok("Address with id - " + id + ", was deleted");
    }
}
