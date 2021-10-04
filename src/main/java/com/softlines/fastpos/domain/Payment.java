package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE Payment SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date date;

    double amount;

    Double discountAmount;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

    @OneToOne(cascade = CascadeType.ALL)
    CashOperation cashOperation;

    @ManyToOne
    Deliveryman deliveryman;

    @ManyToOne(fetch = FetchType.LAZY)
    DailyExpenseReport dailyExpenseReport;

}
