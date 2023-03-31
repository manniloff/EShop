package com.amdaris.mentoring.core.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearchCriteria {
    private String email;
    private List<Long> addresses;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String role;
    private LocalDate startPeriod = LocalDate.of(1900, 1, 1);
    private LocalDate endPeriod = LocalDate.now();
}
