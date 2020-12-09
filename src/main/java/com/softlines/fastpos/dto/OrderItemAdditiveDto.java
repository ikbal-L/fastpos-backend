package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.AdditiveSate;
import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.domain.OrderItemAdditiveId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAdditiveDto {

    @JsonProperty("OrderItemAdditiveId")
    private OrderItemAdditiveId id;

    //    @JsonProperty("OrderItemId")
//    Long orderItemId;
    @JsonProperty(value = "OrderItemId")
    Long orderItemId;

    @JsonProperty(value = "AdditiveIds")
    Long AdditiveIds;

    @JsonProperty(value = "State", required = true)
    @NotNull
    AdditiveSate state;

    @JsonProperty(value = "Timestamp", required = true)
    @NotNull
    Date timestamp;
}
