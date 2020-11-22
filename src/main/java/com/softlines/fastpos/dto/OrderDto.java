package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import lombok.*;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("BuyerId")
    String buyerId;

    @JsonProperty("OrderTime")
    Date orderTime;

    @JsonProperty("ElapsedTime")
    Duration elapsedTime;

    @JsonProperty("Total")
    double total;

    @JsonProperty("SplittedFromId")
    int splittedFromId;

    @JsonProperty("NewTotal")
    double newTotal;

    @JsonProperty("DiscountAmount")
    double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")
    double discountPercentage;

    @JsonProperty("GivenAmount")
    double givenAmount;

    @JsonProperty("ReturnedAmount")
    double returnedAmount;

    @JsonProperty("ProductsVisibility")
    boolean productsVisibility;

    @JsonProperty("AdditivesVisibility")
    boolean additivesVisibility;

    @JsonProperty("Orderstate")
    OrderState orderstate;

    @JsonProperty("Type")
    OrderType type;

    @JsonProperty("OrderItems")
    List<OrderItemDto> orderItems;

    @JsonProperty("TableId")
    Long tableId;

}





