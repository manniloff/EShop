package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.repository.UserRepository;
import com.amdaris.mentoring.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user;
        }
        throw new NoSuchElementException("User with id - " + id + " doesn't exist!");
    }

    @Override
    public long save(User user) {
        Optional<User> findByEmail = userRepository.findByEmail(user.getEmail());

        if (findByEmail.isPresent()) {
            throw new EntityExistsException("User with email - " + user.getEmail() + ", exists!");
        }

        return userRepository.save(user).getId();
    }

    @Override
    public User update(User user, long id) {
        Optional<User> findById = userRepository.findById(id);

        if (findById.isPresent()) {
            return userRepository.save(user);
        }
        throw new NoSuchElementException("User with id - " + id + " doesn't exist!");
    }

    @Override
    public long deleteById(long id) {
        Optional<User> findById = userRepository.findById(id);

        if (findById.isPresent()) {
            userRepository.delete(findById.get());
            return findById.get().getId();
        }
        throw new NoSuchElementException("User with id - " + id + " doesn't exist!");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> findByEmail = userRepository.findByEmail(email);

        if (findByEmail.isPresent()) {
            return findByEmail;
        }
        throw new NoSuchElementException("User with email - " + email + " doesn't exist!");
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        Optional<User> findByEmail = userRepository.findByPhoneNumber(phoneNumber);

        if (findByEmail.isPresent()) {
            return findByEmail;
        }
        throw new NoSuchElementException("User with phone - " + phoneNumber + " doesn't exist!");
    }

    @Override
    public Optional<User> findLastName(String lastName) {
        Optional<User> findByLastName = userRepository.findByLastName(lastName);

        if (findByLastName.isPresent()) {
            return findByLastName;
        }
        throw new NoSuchElementException("User with last name - " + lastName + " doesn't exist!");
    }

    @Override
    public void clear() {
        userRepository.deleteAll();
    }
}
