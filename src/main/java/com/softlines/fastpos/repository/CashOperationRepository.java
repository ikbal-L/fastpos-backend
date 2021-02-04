package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.CashOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashOperationRepository extends JpaRepository<CashOperation,Long> {
}
