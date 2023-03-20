package com.amdaris.mentoring.core.service.impl;

import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.repository.RoleRepository;
import com.amdaris.mentoring.core.service.RoleService;
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
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(short id) {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isPresent()) {
            return role;
        }
        throw new NoSuchElementException("Role with id - " + id + " doesn't exist!");
    }

    @Override
    public Optional<Role> findByRole(String role) {
        Optional<Role> findByRole = roleRepository.findByRoleType(role);

        if (findByRole.isPresent()) {
            return findByRole;
        }
        throw new NoSuchElementException("Role - " + role + " doesn't exist!");
    }

    @Override
    public short save(Role role) {
        Optional<Role> findByRole = roleRepository.findByRoleType(role.getRoleType());

        if (findByRole.isPresent()) {
            throw new EntityExistsException("Role - " + role.getRoleType() + ", exists!");
        }

        return roleRepository.save(role).getId();
    }

    @Override
    public Role update(Role role, short id) {
        Optional<Role> findById = roleRepository.findById(id);

        if (findById.isPresent()) {
            return roleRepository.save(role);
        }
        throw new NoSuchElementException("Role with id - " + id + " doesn't exist!");
    }

    @Override
    public short deleteById(short id) {
        Optional<Role> findById = roleRepository.findById(id);

        if (findById.isPresent()) {
            roleRepository.delete(findById.get());
            return findById.get().getId();
        }
        throw new NoSuchElementException("Role with id - " + id + " doesn't exist!");
    }

    @Override
    public void clear() {
        roleRepository.deleteAll();
    }
}
