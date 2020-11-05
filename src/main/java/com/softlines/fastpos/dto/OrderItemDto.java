package com.softlines.fastpos.dto;

import lombok.*;

import java.util.List;

@Data
public class OrderItemDto {

    long id;
    String name;
    double unitPrice;
    int quantity;
    double total;
    double discountAmount;
    double totalDiscountAmount;
    double discountPercentatge;
    long productId;
    List<Long> idAdditives;
    long orderId;
}
