package com.softlines.fastpos.jwtsecurity.securityrepository;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value= "SELECT distinct r FROM Role r  JOIN FETCH r.privileges where r=?1")
    List<Role> finRole(Role r);

    @Query(value = "SELECT distinct p FROM Role p JOIN FETCH p.privileges")
    List<Role> findAllRolesWithPrivileges();


    @Query(value= "SELECT p FROM Role p JOIN FETCH p.privileges WHERE p.name= ?1")
    Role findByName(String role);
}
