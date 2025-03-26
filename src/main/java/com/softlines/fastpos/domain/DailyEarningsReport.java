package com.softlines.fastpos.domain;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@SQLDelete(sql = "UPDATE orders SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class DailyEarningsReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @Temporal(TemporalType.DATE)
    LocalDateTime issuedDate;


    @OneToMany(mappedBy = "dailyEarningsReport",fetch = FetchType.EAGER)
    @OrderBy("orderTime asc ")
    Set<Order> cashPayments;

    @OneToMany(mappedBy = "dailyEarningsReport",fetch = FetchType.EAGER)
    @OrderBy("orderTime asc ")
    @Where(clause = "state = 'Canceled' ")
    Set<Order> canceledOrders = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER,mappedBy ="dailyEarningsReport")
    @OrderBy("date asc ")
    Set<Payment> payments;

    double cashRegisterInitialAmount ;

    double cashRegisterDepositedAmount ;

    double cashRegisterWithdrawnAmount ;

    double cashRegisterExpectedAmount ;

    double cashRegisterActualAmount ;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    Set<EarningsCategoryGrouping> earningsByCategory;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @OrderBy("orderNumber asc ")
    Set<OrderRefund> refunds;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "report")
    Set<CashRegisterExpense> cashRegisterExpenses;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

}
