package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.dto.UserDto;
import com.amdaris.mentoring.core.dto.converter.UserConverter;
import com.amdaris.mentoring.core.dto.criteria.UserSearchCriteria;
import com.amdaris.mentoring.core.model.User;
import com.amdaris.mentoring.core.repository.AddressRepository;
import com.amdaris.mentoring.core.repository.UserRepository;
import com.amdaris.mentoring.core.service.UserService;
import com.amdaris.mentoring.core.util.PageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    @Override
    public Page<UserDto> findByCriteria(PageView pageView, UserSearchCriteria criteria) {
        List<Long> addresses = Optional.ofNullable(criteria.getAddresses()).orElse(addressRepository.findAllIds());

        return userRepository.findByCriteria(addresses, criteria.getEmail(), criteria.getPhoneNumber(),
                criteria.getFirstName(), criteria.getLastName(), criteria.getRole(), criteria.getStartPeriod(),
                criteria.getEndPeriod(), PageRequest.of(pageView.getPage(), pageView.getPageSize())
                        .withSort(pageView.getSortDirection(), pageView.getSort())

        ).map(user -> UserConverter.toUserDto.apply(user));
    }


    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserConverter.toUserDto.apply(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return UserConverter.toUserDto.apply(user.get());
        }
        throw new NoSuchElementException("User with id - " + id + " doesn't exist!");
    }

    @Override
    public UserDto save(UserDto userDto) {
        Optional<User> findByEmail = userRepository.findByEmail(userDto.getEmail());

        if (findByEmail.isPresent()) {
            throw new EntityExistsException("User with email - " + userDto.getEmail() + ", exists!");
        }
        User user = UserConverter.toUser.apply(userDto);
        return UserConverter.toUserDto.apply(userRepository.save(user));
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        Optional<User> findById = userRepository.findById(id);

        if (findById.isPresent()) {
            User user = UserConverter.toUser.apply(userDto);
            return UserConverter.toUserDto.apply(userRepository.save(user));
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
    public UserDto findByEmail(String email) {
        Optional<User> findByEmail = userRepository.findByEmail(email);

        if (findByEmail.isPresent()) {
            return UserConverter.toUserDto.apply(findByEmail.get());
        }
        throw new NoSuchElementException("User with email - " + email + " doesn't exist!");
    }

    @Override
    public UserDto findByPhoneNumber(String phoneNumber) {
        Optional<User> findByPhone = userRepository.findByPhoneNumber(phoneNumber);

        if (findByPhone.isPresent()) {
            return UserConverter.toUserDto.apply(findByPhone.get());
        }
        throw new NoSuchElementException("User with phone - " + phoneNumber + " doesn't exist!");
    }

    @Override
    public List<UserDto> findByFullName(String fullName) {
        List<User> findByLastName = userRepository.findByFullName(fullName);

        if (!findByLastName.isEmpty()) {
            return findByLastName.stream()
                    .map(user -> UserConverter.toUserDto.apply(user))
                    .collect(Collectors.toList());
        }
        throw new NoSuchElementException("User with full name - " + fullName + " doesn't exist!");
    }

    @Override
    public void clear() {
        userRepository.deleteAll();
    }
}
