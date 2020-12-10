package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.OrderItemAdditive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemAdditiveRepository extends JpaRepository<OrderItemAdditive, Long> {
}
