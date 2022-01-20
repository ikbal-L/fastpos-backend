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
import java.time.LocalDate;
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

        var states = this.filter.getStates();

        var deliverymanIds = this.filter.getDeliverymanIds();

        var customerIds = this.filter.getCustomerIds();


        if (orderTime.isPresent()){


            Predicate orderTimePredicate = this.criteriaBuilder.equal(root.get("orderTime").as(LocalDate.class), orderTime.get().toLocalDate());
            predicates.add(orderTimePredicate);
        }



        if (states.isPresent()){

            var statesPredicate = this.criteriaBuilder.in(root.<OrderState>get("state"));
            states.get().forEach(statesPredicate::value);
            predicates.add(statesPredicate);
        }


        if (deliverymanIds.isPresent() && !deliverymanIds.get().isEmpty()){
            var deliverymanIdsPredicate = this.criteriaBuilder.in(root.<Deliveryman>get("deliveryman").<Long>get("id"));
            deliverymanIds.get().forEach(deliverymanIdsPredicate::value);
            predicates.add(deliverymanIdsPredicate);
        }


        if (customerIds.isPresent()&& !customerIds.get().isEmpty()) {
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
