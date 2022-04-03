package com.softlines.fastpos.dto.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class PaymentFilter extends Filter {

    @JsonProperty("Date")
    Optional<LocalDateTime> date=Optional.empty();

    @JsonProperty("DeliverymanId")
    Optional<Long> deliverymanId=Optional.empty();

    @JsonProperty("DeliverymanIds")
    Optional<List<Long>> deliverymanIds=Optional.empty();


    @JsonProperty("CustomerId")
    Optional<Long> customerId=Optional.empty();

    @JsonProperty("CustomerIds")
    Optional<List<Long>> customerIds=Optional.empty();
}
