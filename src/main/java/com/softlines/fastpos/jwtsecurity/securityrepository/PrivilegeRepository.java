package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Query(value= "SELECT * FROM Privilege WHERE Privilege.name= ?1", nativeQuery = true)
    Privilege findByName(String name);
}
