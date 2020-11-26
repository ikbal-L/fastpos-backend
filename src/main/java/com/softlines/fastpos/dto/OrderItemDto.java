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
    int quantity;

    @JsonProperty("Total")
    @NotNull
    Double total;

    @JsonProperty("DiscountAmount")
    @NotNull
    Double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    @NotNull
    Double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")
    @NotNull
    Double discountPercentage;

    @JsonProperty("ProductId")
    @NotNull
    Long productId;

    @JsonProperty("IdAdditives")
    List<Long> idAdditives;

    @JsonProperty("orderId")
    @NotNull
    Long orderId;


}
