package com.softlines.fastpos.domain;

import lombok.*;
import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@javax.persistence.Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<OrderItem> orderItems;
    @ManyToOne
    @JoinColumn(name = "tables_id")
    Table table;

}

