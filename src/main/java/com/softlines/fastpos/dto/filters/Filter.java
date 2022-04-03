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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.swing.*;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public  class Filter {

    @JsonProperty("PageSize")
    @Builder.Default
    Optional<Integer> pageSize = Optional.empty();

    @JsonProperty("PageIndex")
    @Builder.Default
    Optional<Integer> pageIndex = Optional.empty();

    @JsonProperty("OrderBy")
    @Builder.Default
    Optional<String> orderBy = Optional.empty();

    @JsonProperty("SortOrder")
    @Builder.Default
    Optional<SortOrder> sortOrder = Optional.empty();

    public boolean isPaginationRequested(){
        return pageIndex.isPresent()&& pageSize.isPresent();
    }

}
