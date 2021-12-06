package com.softlines.fastpos.security.securityrepository;

import com.softlines.fastpos.security.securitydomain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value= "SELECT distinct r FROM Role r  JOIN FETCH r.privileges where r=?1")
    List<Role> finRole(Role r);

    @Query(value = "SELECT distinct r FROM Role r JOIN FETCH r.privileges")
    List<Role> findAllRolesWithPrivileges();


    @Query(value= "SELECT distinct p FROM Role p JOIN FETCH p.privileges WHERE p.name= ?1")
    Role findByName(String role);

    @Query(value= "SELECT distinct p FROM Role p JOIN FETCH p.privileges WHERE p.id= ?1")
    Optional<Role> findRoleById(long id);

    @Query(value= "SELECT r,u FROM User u " +
            "JOIN u.roles r " +
            "WHERE u.id = ?1")
    Set<Role> getUserRoles(long userId);
}
