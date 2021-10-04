package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.domain.CashRegisterExpense;
import com.softlines.fastpos.domain.EarningsCategoryGrouping;
import com.softlines.fastpos.domain.OrderRefund;
import com.softlines.fastpos.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class DailyExpenseReportDto {

    Long id;

    Date issuedDate;

    Map<String,Double> CashPayments;

    Set<PaymentDto> deliveryPayments;

    Map<String,Double> expenses;

    List<EarningsCategoryGrouping> earningsByCategory;

    List<OrderRefund> refunds;

    List<CashRegisterExpenseDto> cashRegisterExpenses;

    double cashRegisterInitialAmount ;

    double cashRegisterDepositedAmount ;

    double cashRegisterWithdrawnAmount ;

    double cashRegisterExpectedAmount ;

    double cashRegisterActualAmount ;
}
