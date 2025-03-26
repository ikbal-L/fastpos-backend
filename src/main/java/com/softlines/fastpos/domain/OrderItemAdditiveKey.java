package com.softlines.fastpos.domain;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemAdditiveKey implements Serializable {

    @Column(name = "order_item_id")
    long orderItemId;

    @Column(name = "additive_id")
    long additiveId;

    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;
        if (obj.getClass()!= this.getClass()) return false;
        OrderItemAdditiveKey other = (OrderItemAdditiveKey) obj;
        return (other.getAdditiveId()==this.getAdditiveId()&&other.getOrderItemId()== this.getOrderItemId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
