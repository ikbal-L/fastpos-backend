package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
@Builder
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class OrderItemAdditiveKey implements Serializable {

    @Column(name = "order_item_id")
    long orderItemId;

    @Column(name = "additive_id")
    long additiveId;
}
