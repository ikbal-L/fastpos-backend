package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;

import java.time.LocalDate;
import java.util.*;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Page<Payment> findByDeliveryman_Id(Long deliveryman_Id, Pageable pageable);
    @Query("SELECT  p from Payment p where p.deliveryman.id=?1 and CAST(p.date AS date) = CAST(?2 AS date)")
    List<Payment> findByDeliveryMan_IdAndDate(Long deliveryMan_Id,@Temporal() Date date);

    @Query(value = "select DISTINCT p from Payment p where date_format(p.date,'%Y-%m-%d') = ?1")
    List<Payment> findAllByDate(String date);

    @Query(value = "select DISTINCT p from Payment p where  Cast( p.date as LocalDate) = ?1")
    List<Payment> findAllByDate(LocalDate date);

    @Query(value = "select DISTINCT p from Payment p where date_format(p.date,'%Y-%m-%d %H:I') = ?1")
    List<Payment> findAllBetween(Date start, Date end);
}
