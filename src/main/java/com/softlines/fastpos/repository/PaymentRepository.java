package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Page<Payment> findByDeliveryMan_Id(Long deliveryMan_Id, Pageable pageable);
}
