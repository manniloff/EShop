package com.amdaris.mentoring.core.model;

import com.amdaris.mentoring.core.util.OrderStatus;
import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_info")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID transId;

    private LocalDateTime creationDate;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REMOVE
    })
    @JoinTable(
            name = "product_order_relation",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )
    private List<Product> products;

    private double orderPrice;
}
