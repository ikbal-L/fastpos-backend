package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    LocalDateTime date;

    double amount;

    Double discountAmount;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

    @OneToOne(cascade = CascadeType.ALL)
    CashOperation cashOperation;

    @ManyToOne
    Deliveryman deliveryman;

    @ManyToOne
    Customer customer;
    @Enumerated(EnumType.STRING)
    PaymentSource paymentSource;

    @ManyToOne(fetch = FetchType.LAZY)
    DailyEarningsReport dailyEarningsReport;

}
