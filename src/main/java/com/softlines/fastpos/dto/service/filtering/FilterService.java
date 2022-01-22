package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.filters.Filter;
import com.softlines.fastpos.dto.filters.Page;
import com.softlines.fastpos.dto.filters.SortOrder;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.List;

@Service
public abstract class FilterService<T,F extends Filter<T>> {

    @PersistenceContext
    protected EntityManager em;

    protected F filter;

    protected CriteriaBuilder criteriaBuilder;

    protected TypedQuery<T> query;
    protected TypedQuery<Long> pageCountQuery;

    protected CriteriaQuery<T> criteriaQuery;
    protected CriteriaQuery<Long> pageCountCriteriaQuery;

    protected Root<T> root;

    protected Class<?> entityClass;

    protected List<Predicate> predicates;

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
        if (filter.isPaginationRequested()){
            this.pageCountQuery = em.createQuery(pageCountCriteriaQuery);
        }
    }
    protected abstract void initializePredicates() throws ParseException;
    protected abstract void initializeCriteriaQuery();


    public Page<T>  buildQuery(F filter) throws ParseException {
        this.filter = filter;
        this.criteriaBuilder = em.getCriteriaBuilder();
        initializeCriteriaQuery();
        if (filter.isPaginationRequested()){
            this.pageCountCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        }
        initializePredicates();
        criteriaQuery.where(predicates.toArray(Predicate[]::new)).distinct(true);
        pageCountCriteriaQuery.select(criteriaBuilder.count(pageCountCriteriaQuery.from(entityClass)));
        pageCountCriteriaQuery.where(predicates.toArray(Predicate[]::new)).distinct(true);
        checkSortingCriteria();
        createQuery();
        checkPaginationCriteria();
        Long pageCount = null;
        if (filter.isPaginationRequested()){

            var totalElements = pageCountQuery.getSingleResult();
            if (totalElements<=filter.getPageSize().get()){
                pageCount = 1L;
            }else {
                var pageSize = filter.getPageSize().get();
                if (totalElements% pageSize ==0){
                    pageCount = totalElements/pageSize;
                }else{
                    pageCount = totalElements/pageSize+1;
                }

            }

        }
        var collection = query.getResultList();
        return new Page<>(collection.size(),collection,pageCount);
    }



}
