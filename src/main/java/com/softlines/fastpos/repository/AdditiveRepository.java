package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdditiveRepository extends JpaRepository<Additive, Long> {
    Additive findByDescription(String description);

//    @Query(value = "select distinct a from Additive a LEFT JOIN FETCH  a.products WHERE a.id = ?1")
//    Additive findAdditiveByIdWithProducts();
}
