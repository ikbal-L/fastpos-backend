package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Additive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdditiveRepository extends JpaRepository<Additive, Long> {
//    List<Additive> findByName(String name);

}
