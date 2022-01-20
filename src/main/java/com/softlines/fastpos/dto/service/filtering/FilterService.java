package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.dto.filters.Filter;
import com.softlines.fastpos.dto.filters.Page;
import com.softlines.fastpos.dto.filters.SortOrder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

//    public FilterService(EntityManagerFactory entityManagerFactory) {
//        em = entityManagerFactory.createEntityManager();
//    }

    protected void checkSortingCriteria( ){

        var orderBy = filter.getOrderBy();
        var sortOrder = filter.getSortOrder();


        if (orderBy.isPresent()&& !orderBy.get().isBlank()&& sortOrder.isPresent()){
            if (sortOrder.get() == SortOrder.Asc)criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy.get())));
            if (sortOrder.get() == SortOrder.Desc)criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy.get())));
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
