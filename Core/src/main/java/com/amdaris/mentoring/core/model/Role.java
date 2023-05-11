package com.amdaris.mentoring.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    private String roleType;

    @OneToMany(mappedBy = "role", orphanRemoval = true)
    @JsonIgnoreProperties("role")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
