package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.filters.OrderFilter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderFilterService extends FilterService<Order, OrderFilter>{

    //TODO: Figure out issue of using different entity managers and producing different result (db session not in sync?)

//    public OrderFilterService(EntityManagerFactory entityManagerFactory) {
//        super(entityManagerFactory);
//    }

    @Override
    protected void init() throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        this.root = criteriaQuery.from(Order.class);
        this.root.fetch("orderItems", JoinType.LEFT);

        var orderTime = this.filter.getOrderTime();
        var state = this.filter.getState();
        var states = this.filter.getStates();
        var deliverymanId = this.filter.getDeliverymanId();
        var deliverymanIds = this.filter.getDeliverymanIds();
        var customerId = this.filter.getCustomerId();
        var customerIds = this.filter.getCustomerIds();


        if (orderTime.isPresent()){

            var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            var dateString = simpleDateFormat.format(orderTime.get());
            orderTime = Optional.ofNullable(simpleDateFormat.parse(dateString));
            var exp = this.criteriaBuilder.function("date_format",String.class,root.get("orderTime"),this.criteriaBuilder.literal("%Y-%m-%d"));
            Predicate orderTimePredicate = this.criteriaBuilder.equal(exp, dateString);
            predicates.add(orderTimePredicate);
        }

        if (state.isPresent()&& states.isEmpty()){
            Predicate statePredicate = this.criteriaBuilder.equal(root.get("state"), state.get());
            predicates.add(statePredicate);
        }

        if (states.isPresent()&& state.isEmpty()){

            var statesPredicate = this.criteriaBuilder.in(root.<OrderState>get("state"));
            states.get().forEach(statesPredicate::value);
        }

        if (deliverymanId.isPresent()&& deliverymanIds.isEmpty()){
            Predicate deliverymanIdPredicate = this.criteriaBuilder.equal(root.<Deliveryman>get("deliveryman").<Long>get("id"), deliverymanId.get());
            predicates.add(deliverymanIdPredicate);
        }

        if (deliverymanIds.isPresent() && deliverymanId.isEmpty()){
            var deliverymanIdsPredicate = this.criteriaBuilder.in(root.<Deliveryman>get("deliveryman").<Long>get("id"));
            deliverymanIds.get().forEach(deliverymanIdsPredicate::value);
            predicates.add(deliverymanIdsPredicate);
        }

        if (customerId.isPresent()&& customerIds.isEmpty()){
            Predicate customerIdPredicate = this.criteriaBuilder.equal(root.<Deliveryman>get("customer").<Long>get("id"), customerId.get());
            predicates.add(customerIdPredicate);
        }

        if (customerIds.isPresent() && customerId.isEmpty()){
            var customerIdsPredicate = this.criteriaBuilder.in(root.<Customer>get("customer").<Long>get("id"));
            customerIds.get().forEach(customerIdsPredicate::value);
            predicates.add(customerIdsPredicate);
        }

        //TODO Fix Issue: LEFT JOIN returning multiple instances of the same entity
        criteriaQuery.where(predicates.toArray(Predicate[]::new)).distinct(true);
    }

    @Override
    protected void initCriteriaQuery() {
        this.criteriaQuery = criteriaBuilder.createQuery(Order.class);
    }
}
