package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Payment;
import com.softlines.fastpos.dto.filters.PaymentFilter;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentFilterService extends FilterService<Payment, PaymentFilter> {


//    public PaymentFilterService(EntityManagerFactory entityManagerFactory) {
//        super(entityManagerFactory);
//    }

    @Override
    protected void initializePredicates() throws ParseException {
        this.root = criteriaQuery.from(Payment.class);
        this.entityClass = Payment.class;

        var deliverymanIds = this.filter.getDeliverymanIds();

        var customerIds = this.filter.getCustomerIds();

        var date = filter.getDate();
        if (date.isPresent()){

            Predicate datePredicate = this.criteriaBuilder.equal(root.get("date").as(LocalDate.class), date.get().toLocalDate());
            predicates.add(datePredicate);
        }



        if (deliverymanIds.isPresent()&& !deliverymanIds.get().isEmpty()){
            var deliverymanIdsPredicate = this.criteriaBuilder.in(root.<Deliveryman>get("deliveryman").<Long>get("id"));
            deliverymanIds.get().forEach(deliverymanIdsPredicate::value);
            predicates.add(deliverymanIdsPredicate);
        }



        if (customerIds.isPresent()&& !customerIds.get().isEmpty()){
            var customerIdsPredicate = this.criteriaBuilder.in(root.<Deliveryman>get("customer").<Long>get("id"));
            customerIds.get().forEach(customerIdsPredicate::value);
            predicates.add(customerIdsPredicate);
        }


    }

    @Override
    protected void initializeCriteriaQuery() {
        this.criteriaQuery = criteriaBuilder.createQuery(Payment.class);
    }


}
