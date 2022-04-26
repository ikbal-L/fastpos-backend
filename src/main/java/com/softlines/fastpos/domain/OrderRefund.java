package com.softlines.fastpos.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class OrderRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_number",nullable = false)
    private long orderNumber;

    @Column(nullable = false)
    private double amount;

    private String issuedBy;
    @Builder.Default
    private  boolean partial = false;
}
