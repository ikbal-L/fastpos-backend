package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentSavedDto {
@JsonProperty("PaidOrders")
    List<OrderDto> paidOrders;
    @JsonProperty("NotPaidOrders")
    List<OrderDto> notPaidOrders;
    @JsonProperty("Payment")
    PaymentDto payment;
    @JsonProperty("Deliveryman")

    DeliverymanDto deliveryMan;
}
