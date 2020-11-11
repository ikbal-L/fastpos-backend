package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Person;
import com.softlines.fastpos.domain.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    List<Waiter> findByName(String name);
}
