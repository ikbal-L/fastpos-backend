package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
public class OrderItemDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("UnitPrice")
    double unitPrice;

    @JsonProperty("Quantity")
    int quantity;

    @JsonProperty("Total")
    double total;

    @JsonProperty("DiscountAmount")
    double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")
    double discountPercentage;

    @JsonProperty("ProductId")
    long productId;

    @JsonProperty("IdAdditives")
    List<Long> idAdditives;

    @JsonProperty("orderId")
    long orderId;
}
