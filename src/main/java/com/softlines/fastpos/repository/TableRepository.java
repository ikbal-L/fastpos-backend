package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {

}
