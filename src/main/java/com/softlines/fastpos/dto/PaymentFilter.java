package com.softlines.fastpos.dto;

import com.softlines.fastpos.domain.Payment;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.text.ParseException;

public class PaymentFilter extends Filter<Payment>{
    @Override
    protected void init(CriteriaBuilder cb, EntityManager em) throws ParseException {

    }

    @Override
    protected void CreateCriteriaQuery(CriteriaBuilder cb, EntityManager em) {

    }
}
