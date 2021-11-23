package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.CashRegisterExpense;
import com.softlines.fastpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface CashRegisterExpenseRepository extends JpaRepository<CashRegisterExpense,Long> {
//    @Query(value = "select  DISTINCT e from CashRegisterExpense as e where date_format(e.issuedDate,'%Y-%m-%d') = ?1")

    @Query(value = "select  DISTINCT e from CashRegisterExpense as e where date_format(e.issuedDate,'%Y-%m-%d') = ?1")
    List<CashRegisterExpense> findAllByIssuedDate(String issuedDate);

    @Query(value = "select  DISTINCT e from CashRegisterExpense as e where Cast(e.issuedDate as LocalDate) = ?1")
    List<CashRegisterExpense> findAllByIssuedDate(LocalDate date);

}
