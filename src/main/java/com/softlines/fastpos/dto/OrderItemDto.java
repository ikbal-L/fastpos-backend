package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.validation.ValidationDiscountAmount;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ValidationDiscountAmount(message = "validation.student.password.match")
public class OrderItemDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("UnitPrice")
    @Min(1)
    @NotNull
    Double unitPrice;

    @JsonProperty("Quantity")
    @Min(1)
    @NotNull
    Integer quantity;

    @JsonProperty("Total")
    @NotNull
    Double total;

    @JsonProperty("DiscountAmount")

    Double discountAmount;

    @JsonProperty("TotalDiscountAmount")

    Double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")

    Double discountPercentage;

    @JsonProperty("ProductId")
    @NotNull
    Long productId;

    @JsonProperty("IdAdditives")
    List<Long> idAdditives;

    @JsonProperty("orderId")
      Long orderId;


}
