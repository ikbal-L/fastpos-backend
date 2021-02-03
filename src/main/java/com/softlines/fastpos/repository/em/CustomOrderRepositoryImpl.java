package com.softlines.fastpos.repository.em;


//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.domain.Order;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Transactional(transactionManager = "transactionManager")
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
    public List<Order> getByStates(OrderState[] states, long deliverymanId,boolean ascending) {
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        /// select query

        CriteriaQuery<Order> or = cb.createQuery(Order.class);
        Root<Order> orderRoot = or.from(Order.class);
        or = or.select(orderRoot);
        or = or.where(cb.and(cb.equal(orderRoot.get("deliveryman").get("id"),deliverymanId),orderRoot.get("state").in(states)));
        or = or.orderBy(cb.desc(orderRoot.get("orderTime")));
        TypedQuery<Order> query = em.createQuery(or);

        return   query.getResultList();

    }

    @Override
    public Pair<Long, List<Order>> getAllByDeliveryManAndStatePage(int pageNumber, int pageSize ,long deliverymanId,OrderState[] states) {
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();

        /// select query

        CriteriaQuery<Order> or = cb.createQuery(Order.class);
        Root<Order> orderRoot = or.from(Order.class);
        or = or.select(orderRoot);
        or = or.where(cb.equal(orderRoot.get("deliveryman").get("id"),deliverymanId));
        or = or.orderBy(cb.desc(orderRoot.get("orderTime")));
        or = or.where(cb.and(cb.equal(orderRoot.get("deliveryman").get("id"),deliverymanId),orderRoot.get("state").in(states)));
        TypedQuery<Order> query = em.createQuery(or);
        CriteriaQuery<Long> CountQury = cb.createQuery(Long.class);


        var countRoot = CountQury.from(Order.class);
        CountQury =  CountQury.select(cb.count(countRoot));
        CountQury = CountQury.where(cb.equal(orderRoot.get("deliveryman").get("id"),deliverymanId));



        return new Pair<>(em.createQuery(CountQury).getSingleResult(), query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList());


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
