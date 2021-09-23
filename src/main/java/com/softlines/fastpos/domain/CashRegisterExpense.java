package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE CashRegisterExpense SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "cash_register_expense")
public class CashRegisterExpense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    double amount;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    String employeeName;

    @Column(nullable = false)
    Date issuedDate;

    @ManyToOne
    @JoinColumn(nullable = true)
    DailyExpenseReport report;

    @Builder.Default
    @NotNull
    private boolean deleted = false;
}
