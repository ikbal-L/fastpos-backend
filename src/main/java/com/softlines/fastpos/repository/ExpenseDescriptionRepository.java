package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.ExpenseDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDescriptionRepository extends JpaRepository<ExpenseDescription,Long> {
}
