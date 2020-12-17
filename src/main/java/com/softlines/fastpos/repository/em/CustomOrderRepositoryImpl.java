package com.softlines.fastpos.repository.em;


//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softlines.fastpos.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Order> getAllOrder() {

        EntityManager em = entityManagerFactory.createEntityManager();

        PathBuilder<Order> entityPath = new PathBuilder<>(Order.class, "order");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory.selectFrom(entityPath).fetch();
    }

    @Override
    public Order getOrder(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> order = orderCriteriaQuery.from(Order.class);

        orderCriteriaQuery.select(order);
        orderCriteriaQuery.where(criteriaBuilder.equal(order.get("id"), id));
        Query query = em.createQuery(orderCriteriaQuery);
        TypedQuery<Order> q = em.createQuery(orderCriteriaQuery);
        Order order1 = q.getSingleResult();
        return order1;
    }

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
