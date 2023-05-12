package com.amdaris.mentoring.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private short id;
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JsonIgnoreProperties("categories")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Product> products = new HashSet<>();
}
