package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyExpenseReportInputDataDto {

    @JsonProperty("CashRegisterInitialAmount")
    double cashRegisterInitialAmount;

    @JsonProperty("CashRegisterActualAmount")
    double cashRegisterActualAmount;

    @JsonProperty("Expenses")
    Map<String,Double> expenses;

}
