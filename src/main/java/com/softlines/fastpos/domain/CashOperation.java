package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @OneToOne(mappedBy = "cashOperation")
    CashRegisterExpense cashRegisterExpense;


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        CashOperation that = (CashOperation) o;
//        return id == that.id &&
//                Double.compare(that.amount, amount) == 0 &&
//                deleted == that.deleted &&
//                Objects.equals(payment, that.payment) &&
//                Objects.equals(order, that.order) &&
//                Objects.equals(cashRegisterExpense, that.cashRegisterExpense);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, amount, deleted, payment, order, cashRegisterExpense);
//    }
}
