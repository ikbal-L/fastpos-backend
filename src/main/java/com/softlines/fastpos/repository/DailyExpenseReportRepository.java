package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.DailyExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DailyExpenseReportRepository extends JpaRepository<DailyExpenseReport,Long> {
    @Query(value = "select DISTINCT r from DailyExpenseReport r where date_format(r.issuedDate,'%Y-%m-%d')  = ?1")
    Optional<DailyExpenseReport> findByIssuedDate(String issuedDate);
}
