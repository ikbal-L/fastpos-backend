package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderInfo;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class OrderService {

    @PersistenceContext
    EntityManager em;

    @Language("HQL")
    String orderCountQuery = "select info.orderCount from OrderInfo  info where info.date = :date";
    @Language("HQL")
    String selectOrderInfoQuery = "select info from OrderInfo  info where info.date = :date";


//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;


    private final NumerationService numerationService;
    private final OrderRepository orderRepository;

    public static boolean IsActionPayment(Order source, Order incoming) {
        return source.getState() != OrderState.Payed && incoming.getState() == OrderState.Payed;
    }

    public static boolean IsActionCancel(Order source, Order incoming) {
        return source.getState() != OrderState.Canceled && incoming.getState() == OrderState.Canceled;
    }

    public String maskOrderNumber(Order order) {
        return numerationService.mask(order.getOrderNumber(), "%03d");
    }

    public OrderInfo getOrderInfoOfTheDay() {
        var query = em.createQuery(selectOrderInfoQuery).setParameter("date", LocalDate.now());
        var result = query.getResultList();
        if (result.size() == 0) {

            return createOrderInfoOfTheDay();
        }
        return (OrderInfo) result.get(0);
    }

    public void SaveOrderInfo(OrderInfo info) {
        var session = em.unwrap(Session.class);
        session.saveOrUpdate(info);
    }

    public Integer getOrderCountOfTheDay() {
        var query = em.createQuery(orderCountQuery).setParameter("date", LocalDate.now());
        var result = query.getResultList();
        if (result.size() == 0) {
            createOrderInfoOfTheDay();
            return 0;
        }
        return (Integer) result.get(0);
    }

    protected OrderInfo createOrderInfoOfTheDay() {
        var orderInfo = OrderInfo.builder().date(LocalDate.now()).build();
        em.persist(orderInfo);
        return orderInfo;
    }

    @Transactional(transactionManager = "transactionManager")
    public Order saveOrder(Order order) {
        var orderInfo = getOrderInfoOfTheDay();
        orderInfo.incrementOrderCount();
        order.setOrderNumber(orderInfo.getOrderCount());
        var code = maskOrderNumber(order);
        order.setOrderCode(code);
        var created = orderRepository.saveOrder(order);
        SaveOrderInfo(orderInfo);
        return created;
    }


}
