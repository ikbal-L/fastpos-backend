package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;



@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    double unitPrice;
    int quantity;
    double total;
    double discountAmount;
    double totalDiscountAmount;
    double discountPercentatge;
    @ManyToOne
    Product product;
    @ManyToMany
    List<Additive> additive;
    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;


}
