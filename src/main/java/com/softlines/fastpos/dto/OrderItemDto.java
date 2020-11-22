package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
public class OrderItemDto {

    long id;
    String name;
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
    List<Long> idAdditives;
    long orderId;
}
