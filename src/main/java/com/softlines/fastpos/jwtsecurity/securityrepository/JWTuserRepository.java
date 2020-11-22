package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JWTuserRepository extends JpaRepository<JWTuser, Long> {

    @Query(value= "SELECT DISTINCT u FROM JWTuser u JOIN FETCH u.roles WHERE u.username = ?1")
    JWTuser findByUsername(String username);

    @Query(value= "SELECT DISTINCT u FROM JWTuser u " +
            "JOIN FETCH u.roles r ")
    List<JWTuser> findAllUsers();

    @Query(value = "SELECT distinct u from JWTuser u left join fetch u.annexes where u.id = ?1")
    Optional<JWTuser> findByIdWithAnnexes(long userId);

}
