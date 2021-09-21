package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.CashRegisterExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRegisterExpenseRepository extends JpaRepository<CashRegisterExpense,Long> {

}
