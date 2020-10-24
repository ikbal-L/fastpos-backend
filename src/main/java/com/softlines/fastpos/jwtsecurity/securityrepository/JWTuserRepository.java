package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface JWTuserRepository extends JpaRepository<JWTuser, Long> {
    JWTuser findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value= "INSERT INTO Users_roles (user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addRole(long userId, long roleId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Users_roles WHERE user_id = ?1 AND role_id = ?2", nativeQuery = true)
    void removeRole(long userId, long roleId);
}
