package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.model.User;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();

    Optional<Role> findById(short id);

    Optional<Role> findByRole(String role);

    Role save(Role role);

    Role update(Role role, short id);

    short deleteById(short id);

    void clear();
}
