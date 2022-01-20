package com.softlines.fastpos.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orderItems_additives")
@Entity(name = "OrderItemAdditive")
//@SQLDelete(sql = "UPDATE orderItems_additives SET deleted=true WHERE order_item_id=? AND additive_id=?")
//@Where(clause = "deleted = false")
//@IdClass(OrderItemAdditiveId.class)
public class OrderItemAdditive {

    @EmbeddedId
    private OrderItemAdditiveId id;



    @ManyToOne( )
    @MapsId("orderItemId")
    @JoinColumn(name = "order_item_id")
    OrderItem orderItem;

    @ManyToOne()
    @MapsId("additiveId")
    @JoinColumn(name = "additive_id")
    Additive additive;

    @Enumerated(EnumType.STRING)
    AdditiveSate state;

    Date timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemAdditive that = (OrderItemAdditive) o;
        return id.equals(that.id) &&
                state == that.state &&
                timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, timestamp);
    }
}
