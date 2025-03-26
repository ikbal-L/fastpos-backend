package com.softlines.fastpos.domain;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderItemAdditiveId implements Serializable {


    @Column(name = "additive_id")
    private Long additiveId;

    @Column(name = "orderItem_id")
    private Long orderItemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemAdditiveId that = (OrderItemAdditiveId) o;
        return additiveId.equals(that.additiveId) &&
                orderItemId.equals(that.orderItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(additiveId, orderItemId);
    }
}
