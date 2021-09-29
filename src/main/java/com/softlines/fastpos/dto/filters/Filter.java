package com.softlines.fastpos.dto.filters;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
public  class Filter<T> {

    @JsonProperty("PageSize")
    @Builder.Default
    Optional<Integer> pageSize = Optional.empty();

    @JsonProperty("PageIndex")
    @Builder.Default
    Optional<Integer> pageIndex = Optional.empty();

    @JsonProperty("OrderBy")
    @Builder.Default
    Optional<String> orderBy = Optional.empty();

    @JsonProperty("AscendingOrder")
    @Builder.Default
    Optional<Boolean> ascendingOrder = Optional.empty();

    @JsonProperty("DescendingOrder")
    @Builder.Default
    Optional<Boolean> descendingOrder = Optional.empty();

    protected TypedQuery<T> query;

    protected CriteriaQuery<T> criteriaQuery;

    protected Root<T> root;



}
