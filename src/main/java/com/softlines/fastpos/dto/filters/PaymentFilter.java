package com.softlines.fastpos.dto.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class PaymentFilter extends Filter<Payment>{

    @JsonProperty("Date")
    Optional<Date> date;

    @JsonProperty("DeliverymanId")
    Optional<Long> deliverymanId;

    @JsonProperty("DeliverymanIds")
    Optional<List<Long>> deliverymanIds;
}
