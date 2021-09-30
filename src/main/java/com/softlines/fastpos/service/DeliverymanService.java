package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;

import java.util.List;

public class DeliverymanService {

    public static void calculateBalance(Deliveryman deliveryman , List<Order> orders){
        var sumOfDelivered = orders.stream().filter(order -> order.getState() == OrderState.Delivered && order.getDeliveryman().getId() == deliveryman.getId()).mapToDouble(Order::getNewTotal).sum();
        var sumOfDeliveredPartiallyPaid = orders.stream().filter(order ->
                order.getState() == OrderState.DeliveredPartiallyPaid
                        && order.getDeliveryman().getId() == deliveryman.getId()
        ).mapToDouble(o->o.getNewTotal()-o.getGivenAmount()).sum();
        var balance = sumOfDelivered+sumOfDeliveredPartiallyPaid;
        deliveryman.setBalance(balance);
    }
}
