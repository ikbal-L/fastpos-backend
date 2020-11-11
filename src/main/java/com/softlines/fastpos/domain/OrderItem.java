package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "orderItems_additives",
            joinColumns = @JoinColumn(name = "orderItem_id"),
            inverseJoinColumns = @JoinColumn(name = "additive_id"))
    List<Additive> additive;
    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

}
