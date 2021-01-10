package com.softlines.fastpos.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
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

//    @Id
//    private Long additiveId;
//
//    @Id
//    private Long orderItemId;

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

//    @Builder.Default
//    private boolean deleted = false;

}
