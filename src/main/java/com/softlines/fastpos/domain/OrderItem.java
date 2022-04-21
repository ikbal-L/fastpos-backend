package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE OrderItem SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Entity(name = "OrderItem")
@Table(name = "orderItem")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double unitPrice;

    Double customPrice;

    int quantity;

    double total;

    double discountAmount;

    double totalDiscountAmount;

    String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    Long splitFromOrderItemId;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    List<OrderItemAdditive> orderItemAdditives;

    @ManyToOne(cascade = {})
    @JoinColumn(name = "order_id")
    Order order;

    Date timestamp;

    @Enumerated(EnumType.STRING)
    @NotNull
    OrderItemState state;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                Double.compare(orderItem.unitPrice, unitPrice) == 0 &&
                quantity == orderItem.quantity &&
                Double.compare(orderItem.total, total) == 0 &&
                Double.compare(orderItem.discountAmount, discountAmount) == 0 &&
                Double.compare(orderItem.totalDiscountAmount, totalDiscountAmount) == 0 &&
                deleted == orderItem.deleted &&
                productName.equals(orderItem.productName) &&
                orderItemAdditives.equals(orderItem.orderItemAdditives) &&
                timestamp.equals(orderItem.timestamp) &&
                state == orderItem.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitPrice, quantity, total, discountAmount, totalDiscountAmount, productName, orderItemAdditives, timestamp, state, deleted);
    }
}
