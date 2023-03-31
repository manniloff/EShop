package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.dto.UserDto;
import com.amdaris.mentoring.core.dto.criteria.UserSearchCriteria;
import com.amdaris.mentoring.core.util.PageView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(long id);

    UserDto save(UserDto user);

    UserDto update(UserDto user, long id);

    long deleteById(long id);

    UserDto findByEmail(String email);

    UserDto findByPhoneNumber(String phoneNumber);

    List<UserDto> findByFullName(String fullName);

    void clear();

    Page<UserDto> findByCriteria(PageView pageView, UserSearchCriteria criteria);
}
