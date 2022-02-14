package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.PrintingByCategoryConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintingByCategoryConfigurationRepository extends JpaRepository<PrintingByCategoryConfiguration,Long> {

}
