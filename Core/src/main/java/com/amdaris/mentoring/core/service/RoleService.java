package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.core.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();

    Optional<Role> findById(short id);

    Optional<Role> findByRole(String role);

    short save(Role role);

    short update(Role role, short id);

    short deleteById(short id);

    void clear();
}
