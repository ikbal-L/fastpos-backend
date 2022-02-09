package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;

import java.util.Collection;
import java.util.List;

public class CreditService {

    public static void calculateBalance(Deliveryman deliveryman, Collection<Order> orders) {
        var sumOfDelivered = orders.stream().filter(order -> order.getState() == OrderState.Delivered && order.getDeliveryman().getId() == deliveryman.getId()).mapToDouble(Order::getNewTotal).sum();
        var sumOfDeliveredPartiallyPaid = orders.stream().filter(order ->
                order.getState() == OrderState.DeliveredPartiallyPaid
                        && order.getDeliveryman().getId() == deliveryman.getId()
        ).mapToDouble(o -> o.getNewTotal() - o.getGivenAmount()).sum();
        var balance = sumOfDelivered + sumOfDeliveredPartiallyPaid;
        deliveryman.setBalance(balance);
    }

    public static void calculateBalance(Customer customer, Collection<Order> orders) {
        var sumOfCreditOrders = orders.stream().filter(order -> order.getState() == OrderState.Credit && order.getCustomer().getId() == customer.getId()).mapToDouble(Order::getNewTotal).sum();
        var sumOfCreditPartiallyRePaid = orders.stream().filter(order ->
                order.getState() == OrderState.CreditPartiallyRePaid
                        && order.getCustomer().getId() == customer.getId()
        ).mapToDouble(o -> o.getNewTotal() - o.getGivenAmount()).sum();
        var balance = sumOfCreditOrders + sumOfCreditPartiallyRePaid;
        customer.setBalance(balance);
    }

}
