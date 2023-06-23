package com.amdaris.mentoring.payment.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    private String title;

    private String details;

    @OneToOne(mappedBy = "paymentMethod")
    private PaymentHistory payment;
}
