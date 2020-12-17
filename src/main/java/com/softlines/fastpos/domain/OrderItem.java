package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


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

    int quantity;

    double total;

    double discountAmount;

    double totalDiscountAmount;

    double discountPercentage;
    String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "orderItems_additives",
//            joinColumns = @JoinColumn(name = "orderItem_id"),
//            inverseJoinColumns = @JoinColumn(name = "additive_id"))
//    List<Additive> additive;

//<<<<<<< HEAD
//    @OneToMany(mappedBy = "orderItem",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
//=======
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.MERGE,
             fetch = FetchType.LAZY)
    List<OrderItemAdditive> orderItemAdditives;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    Date timestamp;

    @Enumerated(EnumType.STRING)
    @NotNull
    OrderItemState state;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

//    @PersistenceContext
//    EntityManager em;


//    public void addEmployee(Additive employee, AdditiveSate additiveSate) {
//        OrderItemAdditive association = new OrderItemAdditive();
//        association.setAdditive(employee);
//        association.setOrderItem(this);
//
//        association.setAdditiveId(employee.getId());
//        association.setOrderItemId(this.getId());
//        association.setState(additiveSate);
//        em.persist(association);
//
//        this.orderItemAdditives.add(association);
//        employee.getOrderItemAdditives().add(association);
//    }


}
