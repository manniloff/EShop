package com.amdaris.mentoring.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private Set<Long> addressIds;

    private String phoneNumber;

    private String firstName;

    private String lastName;
    private String role;

    private String password;

    private LocalDate birthday;
}
