package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyExpenseReportInputDataDto {

    @JsonProperty("CashRegisterInitialAmount")
    double cashRegisterInitialAmount;

    @JsonProperty("CashRegisterActualAmount")
    double cashRegisterActualAmount;

    @JsonProperty("Expenses")
    Set<Double> expenses;

}
