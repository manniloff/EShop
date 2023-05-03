package com.amdaris.mentoring.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bucket {

    @Id
    private long id;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "buckets", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JsonIgnoreProperties("buckets")
    @EqualsAndHashCode.Exclude
    private List<Product> products;
}
