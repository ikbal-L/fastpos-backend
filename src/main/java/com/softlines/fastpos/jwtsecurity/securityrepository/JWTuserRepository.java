package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JWTuserRepository extends JpaRepository<JWTuser, Long> {
    JWTuser findByUsername(String username);

    //@Transactional
    @Query(value= "SELECT u FROM JWTuser u JOIN FETCH u.roles")
    List<JWTuser> findAllUsers();

    @Modifying
    @Transactional
    @Query(value= "INSERT INTO Users_roles (user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addRole(long userId, long roleId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Users_roles WHERE user_id = ?1 AND role_id = ?2", nativeQuery = true)
    void removeRole(long userId, long roleId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Users_roles WHERE user_id = ?1", nativeQuery = true)
    void removeRoleConstraint(long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET username = ?1 WHERE id = ?2", nativeQuery = true)
    void updateUserName(String username, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET password = ?1 WHERE id = ?2", nativeQuery = true)
    void updatePassword(String password, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET pin_code = ?1 WHERE id = ?2", nativeQuery = true)
    void updatePinCode(String pinCode, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET first_name = ?1 WHERE id = ?2", nativeQuery = true)
    void updateFirstname(String firstname, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET last_name = ?1 WHERE id = ?2", nativeQuery = true)
    void updateLastname(String lastName, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET email = ?1 WHERE id = ?2", nativeQuery = true)
    void updateEmail(String email, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET enabled = ?1 WHERE id = ?2", nativeQuery = true)
    void updateIsEnabled(Boolean enabled, long userId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Users SET token_expired = ?1 WHERE id = ?2", nativeQuery = true)
    void updateTokenExpired(Boolean tokenExpired, long userId);

    @Modifying
    @Transactional
    @Query(value= "INSERT INTO User_dbinfo (user_id, dbinfo_id) VALUES (?1, ?2)", nativeQuery = true)
    void addDbInfo(long userId, long dbInfoId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM User_dbinfo WHERE user_id = ?1 AND dbinfo_id = ?2", nativeQuery = true)
    void removeDbInfo(long userId, long dbInfoId);
}
