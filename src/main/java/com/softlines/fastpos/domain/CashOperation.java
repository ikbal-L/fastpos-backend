package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
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

    double amount;
    @Builder.Default
    @NotNull
    private boolean deleted=false;


    @OneToOne(mappedBy = "cashOperation")
    Payment payment;

    @OneToOne(mappedBy = "cashOperation")
    Order order;

    @OneToOne(mappedBy = "cashOperation")
    CashRegisterExpense cashRegisterExpense;
}
