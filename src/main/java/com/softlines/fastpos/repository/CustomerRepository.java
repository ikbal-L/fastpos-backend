package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
