package com.softlines.fastpos.dto.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class OrderFilter extends Filter<Order> {
    @JsonProperty("OrderTime")
    Optional<Date> orderTime;

    @JsonProperty("State")
    Optional<OrderState> state;

    @JsonProperty("States")
    Optional<List<OrderState>> states;

    @JsonProperty("DeliverymanId")
    Optional<Long> deliverymanId;

    @JsonProperty("DeliverymanIds")
    Optional<List<Long>> deliverymanIds;


}
