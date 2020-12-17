package com.softlines.fastpos.repository.em;

import com.softlines.fastpos.domain.Order;

import java.util.List;

public interface CustomOrderRepository {

    Order saveOrder(Order o);
    List<Order> saveListOrder(List<Order> orderList);
    List<Order> getAllOrder();
    Order getOrder(Long id);

}
