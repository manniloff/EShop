package com.amdaris.mentoring.core.repository;

import com.amdaris.mentoring.core.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Short> {
    Optional<Role> findByRoleType(String role);
}
