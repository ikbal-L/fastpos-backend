package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Query(value= "SELECT p FROM Privilege p WHERE p.name= ?1")
    Privilege findByName(String name);

    @Query(value= "SELECT p FROM Privilege p " +
            "JOIN p.roles r " +
            "JOIN r.users u " +
            "WHERE u.username = ?1")
    Set<Privilege> findUserPrivileges(String username);
}
