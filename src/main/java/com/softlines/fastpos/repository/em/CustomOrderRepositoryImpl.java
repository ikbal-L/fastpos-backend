package com.softlines.fastpos.repository.em;

import com.softlines.fastpos.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Order saveOrder(Order order) {

        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        Order createdOrder = em.merge(order);
        em.flush();
        em.getTransaction().commit();
        return createdOrder;
    }

    @Override
    public List<Order> saveListOrder(List<Order> orderList) {

        List<Order> ListCreatedOrder = new ArrayList<>();
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        orderList.forEach(
                order -> {
                    ListCreatedOrder.add(em.merge(order));
                    em.flush();
                }
        );

        em.getTransaction().commit();
        return ListCreatedOrder;
    }
}
