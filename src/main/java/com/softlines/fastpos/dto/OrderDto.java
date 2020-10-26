package com.softlines.fastpos.dto;

import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {

    long id;
    String buyerId;
    LocalDateTime orderTime;
    Duration elapsedTime;
    double orderTotal;
    double total;
    int splittedFromId;
    double newTotal;
    double discountAmount;
    double totalDiscountAmount;
    double discountPercentage;
    double givenAmount;
    double returnedAmount;
    boolean productsVisibility;
    boolean additivesVisibility;
    OrderState orderstate;
    OrderType type;
    List<OrderItemDto> orderItems;

//    Session session;
//    Customer customer;
//    Table table;
//    List<OrderStateElement> orderStates;
//    Delivereyman delivereyman;
//    Waiter waiter;

}





