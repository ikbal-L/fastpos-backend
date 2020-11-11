package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Deliveryman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DeliverymanRepository extends JpaRepository<Deliveryman, Long> {
    List<Deliveryman> findByName(String name);
}
