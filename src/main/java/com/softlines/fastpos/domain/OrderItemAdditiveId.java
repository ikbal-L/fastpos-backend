package com.softlines.fastpos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderItemAdditiveId implements Serializable {


    @Column(name = "additive_id")
    private Long additiveId;

    @Column(name = "orderItem_id")
    private Long orderItemId;

    public int hashCode() {
        return (int)(orderItemId + additiveId);
    }

    public boolean equals(Object object) {
        if (object instanceof OrderItemAdditiveId) {
            OrderItemAdditiveId otherId = (OrderItemAdditiveId) object;
            return (otherId.orderItemId == this.orderItemId)
                    && (otherId.additiveId == this.additiveId);
        }
        return false;
    }
}
