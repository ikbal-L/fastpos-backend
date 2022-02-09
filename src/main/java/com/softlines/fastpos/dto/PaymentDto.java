package com.softlines.fastpos.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.domain.PaymentSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class PaymentDto {

    long id;

    LocalDateTime date;

    double amount;

    Double discountAmount;

    Long cashOperationId;

    Long deliverymanId;

    Long customerId;

    List<OrderDto> orders;

    PaymentSource paymentSource;
}
