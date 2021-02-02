package com.softlines.fastpos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
@SuperBuilder
@Data
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


    @OneToOne
    Payment payment;

    @OneToOne
    Order order;
}
