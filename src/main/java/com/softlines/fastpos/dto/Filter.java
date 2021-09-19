package com.softlines.fastpos.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.Optional;
@Data
@AllArgsConstructor
@NoArgsConstructor

public abstract class Filter<T> {

    @JsonProperty("PageSize")
    protected Optional<Integer> pageSize;

    @JsonProperty("PageIndex")
    protected Optional<Integer> pageIndex;

    @JsonProperty("OrderBy")
    protected Optional<String> orderBy;

    @JsonProperty("AscendingOrder")
    protected Optional<Boolean> ascendingOrder;

    @JsonProperty("DescendingOrder")
    protected Optional<Boolean> descendingOrder;

    protected TypedQuery<T> query;

    protected CriteriaQuery<T> criteriaQuery;

    protected Root<T> root;

    protected void checkSortingCriteria(CriteriaBuilder cb ){

        if (orderBy.isPresent()&& !orderBy.get().isBlank()){
            if (ascendingOrder.isPresent()&& descendingOrder.isEmpty()&& ascendingOrder.get()){
                criteriaQuery.orderBy(cb.asc(root.get(orderBy.get())));
            }else if(descendingOrder.isPresent()&& ascendingOrder.isEmpty()&& descendingOrder.get()){
                criteriaQuery.orderBy(cb.desc(root.get(orderBy.get())));
            }
        }
    }

    protected void checkPaginationCriteria(){
        if (pageIndex.isPresent()&& pageSize.isPresent()){
            query.setFirstResult(pageIndex.get()*pageSize.get());
            query.setMaxResults(pageSize.get());
        }
    }
    protected void createQuery(EntityManager em){
        this.query = em.createQuery(criteriaQuery);
    }
    protected abstract void init(CriteriaBuilder cb, EntityManager em) throws ParseException;
    protected abstract void CreateCriteriaQuery(CriteriaBuilder cb, EntityManager em);

    public TypedQuery<T> buildQuery(CriteriaBuilder cb, EntityManager em) throws ParseException {
        CreateCriteriaQuery(cb, em);
        init(cb,em);
        checkSortingCriteria(cb);
        createQuery(em);
        checkPaginationCriteria();
        return query;
    }

}
