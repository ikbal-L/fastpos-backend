package com.softlines.fastpos.dto.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class OrderFilter extends Filter<Order> {
    @JsonProperty("OrderTime")
    @Builder.Default
    Optional<Date> orderTime = Optional.empty();

    @JsonProperty("State")
    Optional<OrderState> state = Optional.empty();

    @JsonProperty("States")
    @Builder.Default
    Optional<List<OrderState>> states= Optional.empty();

    @JsonProperty("DeliverymanId")
    @Builder.Default
    Optional<Long> deliverymanId= Optional.empty();

    @JsonProperty("DeliverymanIds")
    @Builder.Default
    Optional<List<Long>> deliverymanIds= Optional.empty();


}
