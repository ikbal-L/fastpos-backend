package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class Order extends SyncEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "order_number",nullable = false)
    Integer orderNumber;

    @Column(name = "order_code")
    String orderCode;

    LocalDateTime orderTime;


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

    @OneToMany( mappedBy = "order",orphanRemoval=true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "tables_id")
    Table table;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "deliveryman_id")
    Deliveryman deliveryman;

    @ManyToOne
    @JoinColumn(name = "waiter_id")
    Waiter waiter;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToOne
    CashOperation cashOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    DailyEarningsReport dailyEarningsReport;
    @Column(name = "canceled_by")
    String canceledBy;



    public void setCanceledInfo(OrderState previousState,String canceledBy){
        if (previousState!= OrderState.Canceled){
            state = OrderState.Canceled;
            this.canceledBy = canceledBy;
        }
    }


}

