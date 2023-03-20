package com.amdaris.mentoring.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JsonIgnoreProperties("categories")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();
}
