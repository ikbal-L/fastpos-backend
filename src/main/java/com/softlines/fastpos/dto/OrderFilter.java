package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderFilter {
    @JsonProperty("OrderTime")
    Optional<Date> orderTime;

    @JsonProperty("State")
    Optional<OrderState> state;

    public Map<String,Object> getCriteria(){
        Map<String,Object> criteria = new HashMap<>();
        orderTime.ifPresent(date -> criteria.put("orderTime", date));
        state.ifPresent(orderState -> criteria.put("state", orderState));
        return  criteria;
    }

    public TypedQuery<Order> getQuery(CriteriaBuilder cb, EntityManager em) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> order = cq.from(Order.class);

        ParameterExpression<Date> parameter = cb.parameter(Date.class,"orderTime");
        if (orderTime.isPresent()){

            var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            var dateString = simpleDateFormat.format(orderTime.get());
            orderTime = Optional.ofNullable(simpleDateFormat.parse(dateString));
            var exp = cb.function("date_format",String.class,order.get("orderTime"),cb.literal("%Y-%m-%d"));
            Predicate orderTimePredicate = cb.equal(exp, dateString);
            predicates.add(orderTimePredicate);
        }

        if (state.isPresent()){
            Predicate statePredicate = cb.equal(order.get("state"), state.get());
            predicates.add(statePredicate);
        }

        cq.where(predicates.toArray(Predicate[]::new));

        TypedQuery<Order> query = em.createQuery(cq);
//        query.setParameter("orderTime",orderTime.get());
        return  query;
    }
}
