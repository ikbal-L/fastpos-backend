package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DeliverymanRepository extends JpaRepository<Deliveryman, Long> {
    @Query(value="select w from Deliveryman w WHERE w.active = ?1")
    List<Deliveryman>  findAllActiveDeliverymen(boolean active);
}
