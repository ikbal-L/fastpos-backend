package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTuserRepository extends JpaRepository<JWTuser, Long> {
    JWTuser findByUsername(String username);
}
