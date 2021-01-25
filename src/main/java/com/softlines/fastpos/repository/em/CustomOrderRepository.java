package com.softlines.fastpos.repository.em;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import java.util.List;
import org.javatuples.Pair;

public interface CustomOrderRepository {

    Order saveOrder(Order o);
    List<Order> saveListOrder(List<Order> orderList);
    List<Order> getAllOrder();
    Order getOrder(Long id);
    Pair<Long,List<Order>> getByStates(OrderState[] states, long deliverymanId, int pageNumber, int pageSize);
    Pair<Long,List<Order>> getAllByDeliveryManPage(int pageNumber, int pageSize, long deliverymanId);
}
