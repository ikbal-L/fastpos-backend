package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value= "SELECT * FROM Role WHERE Role.name= ?1", nativeQuery = true)
    Role findByName(String role);

    @Modifying
    @Transactional
    @Query(value= "INSERT INTO Roles_privileges (role_id, privilege_id) VALUES (?1, ?2)", nativeQuery = true)
    void addPrivilege(long roleId, long privilegeId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Roles_privileges WHERE role_id = ?1 AND privilege_id = ?2", nativeQuery = true)
    void removePrivilege(long roleId, long privilegeId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Roles_privileges WHERE role_id = ?1", nativeQuery = true)
    void removePrivilegeConstraint(Long roleId);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Users_roles WHERE role_id = ?1", nativeQuery = true)
    void removeUserConstraint(Long roleId);

}
