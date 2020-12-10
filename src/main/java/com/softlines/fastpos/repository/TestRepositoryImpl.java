package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Order;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class TestRepositoryImpl implements TestRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
//    @Transactional
    public void customMethod(Order order) {
        em.refresh(order);
    }

}
