package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orderItems_additives")
@Entity(name = "OrderItemAdditive")
//@IdClass(OrderItemAdditiveId.class)
public class OrderItemAdditive {

    @EmbeddedId
    private OrderItemAdditiveId id;

//    @Id
//    private Long additiveId;
//
//    @Id
//    private Long orderItemId;

    @ManyToOne
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

}
