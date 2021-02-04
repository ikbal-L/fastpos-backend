package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;

import java.util.*;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Page<Payment> findByDeliveryMan_Id(Long deliveryMan_Id, Pageable pageable);
    @Query("SELECT  p from Payment p where p.deliveryMan.id =?1 and CAST(p.date AS date) = CAST(?2 AS date)")
    List<Payment> findByDeliveryMan_IdAndDate(Long deliveryMan_Id,@Temporal() Date date);
}
