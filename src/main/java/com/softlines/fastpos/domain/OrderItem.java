package com.softlines.fastpos.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

@SQLDelete(sql = "UPDATE OrderItem SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double unitPrice;

    int quantity;

    double total;

    double discountAmount;

    double totalDiscountAmount;

    double discountPercentage;

    String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "orderItems_additives",
//            joinColumns = @JoinColumn(name = "orderItem_id"),
//            inverseJoinColumns = @JoinColumn(name = "additive_id"))
//    List<Additive> additive;

    @OneToMany(mappedBy = "orderItem",cascade = {CascadeType.ALL})
    List<OrderItemAdditive> orderItemAdditives;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    Date timestamp;

    @Enumerated(EnumType.STRING)
    @NotNull
    OrderItemState state;

    @Builder.Default
    @NotNull
    private boolean deleted=false;




}
