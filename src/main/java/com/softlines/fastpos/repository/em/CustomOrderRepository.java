package com.softlines.fastpos.repository.em;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.domain.OrderItemAdditive;
import com.softlines.fastpos.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomOrderRepository {
    Order saveOrder(Order o) ;
    List<Order> saveListOrder(List<Order> orderList);
}
