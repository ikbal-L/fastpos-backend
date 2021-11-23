package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.DailyEarningsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface DailyExpenseReportRepository extends JpaRepository<DailyEarningsReport,Long> {
    @Query(value = "select DISTINCT r from DailyEarningsReport r where date_format(r.issuedDate,'%Y-%m-%d')  = ?1")
    Optional<DailyEarningsReport> findByIssuedDate(String issuedDate);


    @Query(value = "select DISTINCT r from DailyEarningsReport r where Cast(r.issuedDate as LocalDate)  = ?1")
    Optional<DailyEarningsReport> findByIssuedDate(LocalDate issuedDate);

//    @Query(value = "select DISTINCT r from DailyEarningsReport r order by r.issuedDate desc LIMIT 1")
    Optional<DailyEarningsReport>  findFirstByOrderByIssuedDateDesc();
}
