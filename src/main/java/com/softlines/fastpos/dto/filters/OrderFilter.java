package com.softlines.fastpos.dto.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.domain.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class OrderFilter extends Filter {

    @Builder.Default
    Optional<LocalDateTime> orderTime = Optional.empty();

    @Builder.Default
    Optional<List<OrderState>> states= Optional.empty();

    @Builder.Default
    Optional<List<Long>> deliverymanIds= Optional.empty();

    @Builder.Default
    Optional<List<Long>> customerIds= Optional.empty();


}
