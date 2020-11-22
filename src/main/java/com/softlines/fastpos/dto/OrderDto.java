package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {

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
    boolean productsVisibility;
    boolean additivesVisibility;
    OrderState orderstate;
    @JsonProperty("Type")
    OrderType type;
    @JsonProperty("OrderItems")
    List<OrderItemDto> orderItems;
    @JsonProperty("TableId")
    Long tableId;

//    Session session;
//    Customer customer;
//    Table table;
//    List<OrderStateElement> orderStates;
//    Deliveryman delivereyman;
//    Waiter waiter;

}





