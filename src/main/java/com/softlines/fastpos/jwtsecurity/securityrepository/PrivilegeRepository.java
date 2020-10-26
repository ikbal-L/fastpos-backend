package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Query(value= "SELECT * FROM Privilege WHERE Privilege.name= ?1", nativeQuery = true)
    Privilege findByName(String name);

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM Roles_privileges WHERE privilege_id = ?1", nativeQuery = true)
    void removeConstraint(Long privilegeId);

    @Modifying
    @Transactional
    @Query(value= "UPDATE Privilege SET name = ?1 WHERE id = ?2", nativeQuery = true)
    void updatePrivilegeName(String newName, long privilegeId);
}
