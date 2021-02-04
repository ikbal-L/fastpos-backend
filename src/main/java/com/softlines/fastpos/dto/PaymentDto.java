package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @JsonProperty("Id")
    long id;
    @JsonProperty("Date")
    Date date;
    @JsonProperty("Amount")
    double amount;
    @JsonProperty("CashOperationId")
    Long cashOperationId;
    @JsonProperty("DeliveryManId")
    Long  deliveryManId;
}
