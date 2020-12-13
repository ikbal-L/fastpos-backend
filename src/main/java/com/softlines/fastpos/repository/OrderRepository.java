package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.repository.em.CustomOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> ,CustomOrderRepository{

    @Query(value = "select DISTINCT o from Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH o.table")
    List<Order> findAllOrdersWithOrderItems();

    @Query(value = "select DISTINCT o from Order o LEFT JOIN FETCH o.orderItems LEFT JOIN FETCH o.table WHERE o.id = ?1")
    Order findByIdOrderWithOrderItems(long id);

    @Query(value = "select DISTINCT o from Order o LEFT JOIN FETCH o.orderItems LEFT JOIN FETCH o.table WHERE o.id IN (:ids)")
    List<Order> findManyOrderWithOrderItems(List<Long> ids);
}
