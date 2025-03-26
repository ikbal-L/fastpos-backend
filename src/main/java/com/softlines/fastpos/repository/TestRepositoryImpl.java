package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class TestRepositoryImpl implements TestRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void customMethod(Order order) {

        em.refresh(order);
    }

}
