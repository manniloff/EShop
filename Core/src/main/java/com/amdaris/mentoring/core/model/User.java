package com.amdaris.mentoring.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class User {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REMOVE
    })
    @JoinTable(
            name = "user_address_relation",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "address_id", referencedColumnName = "id")}
    )
    @EqualsAndHashCode.Exclude
    private Set<Address> addresses = new HashSet<>();

    private String phoneNumber;

    private String firstName;

    private String lastName;

    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnoreProperties("users")
    private Role role;

    private String password;

    private LocalDate birthday;
}
