package com.softlines.fastpos.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE orders SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
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

    @OneToMany(mappedBy = "order",orphanRemoval=true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "tables_id")
    Table table;

    @Builder.Default
    @NotNull
    private boolean deleted = false;
}

