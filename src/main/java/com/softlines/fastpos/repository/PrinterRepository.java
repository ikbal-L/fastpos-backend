package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Printer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrinterRepository extends JpaRepository<Printer,Long> {
}
