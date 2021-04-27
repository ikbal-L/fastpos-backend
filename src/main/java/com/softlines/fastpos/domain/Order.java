package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
@SQLDelete(sql = "UPDATE orders SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@javax.persistence.Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)

public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "order_number",nullable = false)
    long orderNumber;

    @Temporal(TemporalType.DATE)
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
    @NotNull
    OrderState state;

    @Enumerated(EnumType.STRING)
    OrderType type;

    @OneToMany( mappedBy = "order",orphanRemoval=true,
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "tables_id",nullable = true)
    Table table;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "deliveryman_id",nullable = true)
    Deliveryman deliveryman;

    @ManyToOne
    @JoinColumn(name = "waiter_id",nullable = true)
    Waiter waiter;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = true)
    Customer customer;

    @OneToOne
    CashOperation cashOperation;
}

