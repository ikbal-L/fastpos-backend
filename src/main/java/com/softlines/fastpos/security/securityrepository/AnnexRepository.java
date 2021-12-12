package com.softlines.fastpos.security.securityrepository;

import com.softlines.fastpos.security.securitydomain.Annex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnnexRepository extends JpaRepository<Annex,Long> {

    @Query(value= "SELECT a FROM Annex a WHERE a.name= ?1")
    Annex findByName(String name);


}
