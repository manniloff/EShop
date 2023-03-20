package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(long id);

    User save(User user);

    User update(User user, long id);

    long deleteById(long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findLastName(String lastName);

    void clear();
}
