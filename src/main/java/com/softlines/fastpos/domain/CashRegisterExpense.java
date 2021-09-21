package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE CashRegisterExpense SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
public class CashRegisterExpense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    double amount;

    @Column(nullable = false)
    String employeeName;

    @Builder.Default
    @NotNull
    private boolean deleted = false;
}
