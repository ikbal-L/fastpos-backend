package com.softlines.fastpos.domain;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@SQLDelete(sql = "UPDATE orders SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class DailyExpenseReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Temporal(TemporalType.DATE)
    Date issuedDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "receipt_id")
    @Column(name = "cash_payment_amount")
    Map<String,Double> CashPayments;

    @ElementCollection(fetch = FetchType.EAGER)
    Map<String,Double> deliveryPayments;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "misc_expenses")
    @MapKeyColumn(name = "expense_description")
    @Column(name = "expense_amount")
    Map<String,Double> expenses;

    double cashRegisterInitialAmount ;

    double cashRegisterDepositedAmount ;

    double cashRegisterWithdrawnAmount ;

    double cashRegisterExpectedAmount ;

    double cashRegisterActualAmount ;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<EarningsCategoryGrouping> earningsByCategory;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

}
