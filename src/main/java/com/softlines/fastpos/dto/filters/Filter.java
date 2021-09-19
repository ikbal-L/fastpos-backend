package com.softlines.fastpos.dto.filters;
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



}
