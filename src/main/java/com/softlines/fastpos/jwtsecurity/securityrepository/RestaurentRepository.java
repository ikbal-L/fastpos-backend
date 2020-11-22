package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Restaurent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurentRepository extends JpaRepository<Restaurent, Long> {

    @Query(value="select distinct p from Restaurent p  LEFT JOIN FETCH  p.annexes WHERE p.name = ?1")
    List<Restaurent> findByName(String name);

    @Query(value="select distinct p from Restaurent p LEFT JOIN FETCH  p.annexes")
    List<Restaurent> findAllRestaurentWithAnnexes();

    @Query(value="select distinct p from Restaurent p LEFT JOIN FETCH  p.annexes WHERE p.id = ?1")
    Restaurent findByIdRestaurentWithAnnexes(long id);

    @Query(value="select distinct p from Restaurent p LEFT JOIN FETCH  p.annexes where p.id IN :ids")
    List<Restaurent> findManyRestaurentWithAnnexes(@Param("ids") List<Long> RestaurentIds);

    
}
