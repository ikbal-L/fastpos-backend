package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE cashoperation SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
public class CashOperation extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false)
    @Builder.Default
    LocalDateTime issuedDate = LocalDateTime.now();

    double amount;
    @Builder.Default
    @NotNull
    private boolean deleted=false;


    @OneToOne(mappedBy = "cashOperation")
    Payment payment;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @OneToOne(mappedBy = "cashOperation")
    CashRegisterExpense cashRegisterExpense;

    @Enumerated(EnumType.STRING)
    CashOperationType type;

}


