package com.softlines.fastpos.domain;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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
    Map<String,Double> CashPayments;

    @ElementCollection(fetch = FetchType.EAGER)
    Set<Double> deliveryPayments;

    @ElementCollection(fetch = FetchType.EAGER)
    Set<Double> expenses;

    double cashRegisterInitialAmount ;

    double cashRegisterDepositedAmount ;

    double cashRegisterWithdrawnAmount ;

    double cashRegisterExpectedAmount ;

    double cashRegisterActualAmount ;

}
