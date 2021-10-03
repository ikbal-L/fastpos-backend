package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    @JsonProperty("DiscountAmount")
    Double discountAmount;
    @JsonProperty("CashOperationId")
    Long cashOperationId;
    @JsonProperty("DeliveryManId")
    Long  deliveryManId;
    @JsonProperty("Orders")
    List<OrderDto> orders;
}
