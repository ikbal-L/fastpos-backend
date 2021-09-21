package com.softlines.fastpos.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashRegisterExpenseDto {
    @JsonProperty("Id")
    Long id;

    @JsonProperty("Amount")
    double amount;

    @JsonProperty("Description")
    @NotBlank
    String description;

    @JsonProperty("Employee")
    @NotBlank
    String employeeName;
}
