package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Annex;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnnexRepository extends JpaRepository<Annex,Long> {

    @Query(value= "SELECT a FROM Annex a WHERE a.name= ?1")
    Annex findByName(String name);
}
