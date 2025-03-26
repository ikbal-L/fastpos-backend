package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class DailyEarningsReportDto {

    Long id;

    LocalDateTime issuedDate;

    Set<OrderDto> cashPayments = new LinkedHashSet<>();

    Set<ReportOrderData> canceledOrders = new LinkedHashSet<>();

    Set<PaymentDto> deliveryPayments;

    Set<PaymentDto> creditRePayments;


    List<EarningsCategoryGrouping> earningsByCategory;

    List<OrderRefund> refunds;

    List<CashRegisterExpenseDto> cashRegisterExpenses;

    double cashRegisterInitialAmount ;

    double cashRegisterDepositedAmount ;

    double cashRegisterWithdrawnAmount ;

    double cashRegisterExpectedAmount ;

    double cashRegisterActualAmount ;
}
