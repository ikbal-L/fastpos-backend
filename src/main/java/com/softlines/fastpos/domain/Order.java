package com.softlines.fastpos.domain;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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
    Date orderTime;
    LocalTime elapsedTime;
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
    @Enumerated(EnumType.STRING)
    OrderState orderstate;
    @Enumerated(EnumType.STRING)
    OrderType type;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;
    @ManyToOne
    @JoinColumn(name = "tables_id")
    Table table;

}

