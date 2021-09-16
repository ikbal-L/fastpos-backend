package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderFilter {
    @JsonProperty("OrderTime")
    Optional<Date> orderTime;

    @JsonProperty("State")
    Optional<OrderState> state;

    public Map<String,Object> getCriteria(){
        Map<String,Object> criteria = new HashMap<>();
        orderTime.ifPresent(date -> criteria.put("orderTime", date));
        state.ifPresent(orderState -> criteria.put("state", orderState));
        return  criteria;
    }
}
