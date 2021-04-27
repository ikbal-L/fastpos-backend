package com.softlines.fastpos.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
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

    Map<String,Double> deliveryPayments;

    Map<String,Double> expenses;

    double cashRegisterInitialAmount ;

    double cashRegisterDepositedAmount ;

    double cashRegisterWithdrawnAmount ;

    double cashRegisterExpectedAmount ;

    double cashRegisterActualAmount ;
}
