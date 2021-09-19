package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class OrderFilter extends Filter<Order> {
    @JsonProperty("OrderTime")
    Optional<Date> orderTime;

    @JsonProperty("State")
    Optional<OrderState> state;

    @JsonProperty("States")
    Optional<List<OrderState>> states;

    @JsonProperty("DeliverymanId")
    Optional<Long> deliverymanId;

    @JsonProperty("DeliverymanIds")
    Optional<List<Long>> deliverymanIds;


    @Override
    protected void  init(CriteriaBuilder cb, EntityManager em) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        this.root = criteriaQuery.from(Order.class);

        if (orderTime.isPresent()){

            var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            var dateString = simpleDateFormat.format(orderTime.get());
            orderTime = Optional.ofNullable(simpleDateFormat.parse(dateString));
            var exp = cb.function("date_format",String.class,root.get("orderTime"),cb.literal("%Y-%m-%d"));
            Predicate orderTimePredicate = cb.equal(exp, dateString);
            predicates.add(orderTimePredicate);
        }

        if (state.isPresent()&& states.isEmpty()){
            Predicate statePredicate = cb.equal(root.get("state"), state.get());
            predicates.add(statePredicate);
        }

        if (states.isPresent()&& state.isEmpty()){

            var statesPredicate = cb.in(root.<OrderState>get("state"));
            states.get().forEach(statesPredicate::value);
        }

        if (deliverymanId.isPresent()&& deliverymanIds.isEmpty()){
            Predicate deliverymanIdPredicate = cb.equal(root.<Deliveryman>get("deliveryman").<Long>get("id"), deliverymanId.get());
            predicates.add(deliverymanIdPredicate);
        }

        if (deliverymanIds.isPresent() && deliverymanId.isEmpty()){
            var deliverymanIdsPredicate = cb.in(root.<Deliveryman>get("deliveryman").<Long>get("id"));
            deliverymanIds.get().forEach(deliverymanIdsPredicate::value);
            predicates.add(deliverymanIdsPredicate);
        }

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
    }

    @Override
    protected void CreateCriteriaQuery(CriteriaBuilder cb, EntityManager em) {
        this.criteriaQuery = cb.createQuery(Order.class);
    }

}
