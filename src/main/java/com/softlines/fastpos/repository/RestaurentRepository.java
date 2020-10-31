package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Restaurent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurentRepository extends JpaRepository<Restaurent, Long> {

    List<Restaurent> findByName(String name);

}
