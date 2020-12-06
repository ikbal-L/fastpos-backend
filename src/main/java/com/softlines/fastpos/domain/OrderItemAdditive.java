package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderItems_additives")
@IdClass(OrderItemAdditiveKey.class)
public class OrderItemAdditive {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
//    @EmbeddedId
//    OrderItemAdditiveKey id = new OrderItemAdditiveKey() ;

    @Id
    private long orderItemId;
    @Id
    private long additiveId;

    @ManyToOne()
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
