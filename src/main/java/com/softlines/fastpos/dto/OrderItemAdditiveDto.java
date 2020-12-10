package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.AdditiveSate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAdditiveDto {

    @JsonProperty(value = "OrderItemId")
    Long orderItemId;

    @JsonProperty(value = "AdditiveId")
    Long additiveId;

    @JsonProperty(value = "State", required = true)
    @NotNull
    AdditiveSate state;

    @JsonProperty(value = "Timestamp", required = true)
    @NotNull
    Date timestamp;

}
