package com.amdaris.mentoring.payment.model;

import lombok.*;

import javax.persistence.*;

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
