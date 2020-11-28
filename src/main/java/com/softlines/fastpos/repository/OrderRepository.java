package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(value="select DISTINCT o from Order o LEFT JOIN FETCH o.orderItems LEFT JOIN FETCH o.table")
    List<Order> findAllOrdersWithOrderItems();

    @Query(value="select DISTINCT o from Order o LEFT JOIN FETCH o.orderItems LEFT JOIN FETCH o.table WHERE o.id = ?1")
    Order findByIdOrderWithOrderItems(long id);

}
