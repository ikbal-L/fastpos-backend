package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JWTuserRepository extends JpaRepository<JWTuser, Long> {

    @Query(value= "SELECT u FROM JWTuser u JOIN FETCH u.roles WHERE u.username = ?1")
    JWTuser findByUsername(String username);

    //@Transactional
    @Query(value= "SELECT u FROM JWTuser u JOIN FETCH u.roles")
    List<JWTuser> findAllUsers();

}
