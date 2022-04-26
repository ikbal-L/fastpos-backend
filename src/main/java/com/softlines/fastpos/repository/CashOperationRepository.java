package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.CashRegisterExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashOperationRepository extends JpaRepository<CashOperation,Long> {

    @Query(value = "select  DISTINCT e from CashOperation as e where Cast(e.issuedDate as LocalDate) = ?1")
    List<CashOperation> findAllByIssuedDate(LocalDate date);
}
