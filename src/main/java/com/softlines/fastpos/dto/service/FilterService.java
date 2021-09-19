package com.softlines.fastpos.dto.service;

import com.softlines.fastpos.dto.Filter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.ParseException;

@Service
public abstract class FilterService<T,F extends Filter<T>> {
    @PersistenceContext
    protected EntityManager em;
    protected F filter;
    protected CriteriaBuilder criteriaBuilder;

    protected TypedQuery<T> query;

    protected CriteriaQuery<T> criteriaQuery;

    protected Root<T> root;

    protected void checkSortingCriteria( ){

        var orderBy = filter.getOrderBy();
        var ascendingOrder = filter.getAscendingOrder();
        var descendingOrder = filter.getDescendingOrder();

        if (orderBy.isPresent()&& !orderBy.get().isBlank()){
            if (ascendingOrder.isPresent()&& descendingOrder.isEmpty()&& ascendingOrder.get()){
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy.get())));
            }else if(descendingOrder.isPresent()&& ascendingOrder.isEmpty()&& descendingOrder.get()){
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy.get())));
            }
        }
    }

    protected void checkPaginationCriteria(){
        var pageIndex = filter.getPageIndex();
        var pageSize = filter.getPageSize();

        if (pageIndex.isPresent()&& pageSize.isPresent()){
            query.setFirstResult(pageIndex.get()*pageSize.get());
            query.setMaxResults(pageSize.get());
        }
    }
    protected void createQuery(){
        this.query = em.createQuery(criteriaQuery);
    }
    protected abstract void init() throws ParseException;
    protected abstract void initCriteriaQuery();

    public TypedQuery<T> buildQuery(F filter) throws ParseException {
        this.filter = filter;
        this.criteriaBuilder = em.getCriteriaBuilder();
        initCriteriaQuery();
        init();
        checkSortingCriteria();
        createQuery();
        checkPaginationCriteria();
        return query;
    }
}
