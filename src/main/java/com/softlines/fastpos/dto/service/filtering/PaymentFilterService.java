package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Payment;
import com.softlines.fastpos.dto.filters.PaymentFilter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;
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
    protected void init() throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        this.root = criteriaQuery.from(Payment.class);

        var deliverymanId = this.filter.getDeliverymanId();
        var deliverymanIds = this.filter.getDeliverymanIds();

        var customerId = this.filter.getCustomerId();
        var customerIds = this.filter.getCustomerIds();

        var date = filter.getDate();
        if (date.isPresent()){

            Predicate datePredicate = this.criteriaBuilder.equal(root.get("date").as(LocalDate.class), date.get().toLocalDate());
            predicates.add(datePredicate);
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
            var customerIdsPredicate = this.criteriaBuilder.in(root.<Deliveryman>get("customer").<Long>get("id"));
            customerIds.get().forEach(customerIdsPredicate::value);
            predicates.add(customerIdsPredicate);
        }

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
    }

    @Override
    protected void initCriteriaQuery() {
        this.criteriaQuery = criteriaBuilder.createQuery(Payment.class);
    }
}
