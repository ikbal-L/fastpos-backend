package com.softlines.fastpos.security.securityrepository;

import com.softlines.fastpos.security.securitydomain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value= "SELECT DISTINCT u FROM User u JOIN FETCH u.roles WHERE u.username = ?1")
    User findByUsername(String username);

    @Query(value= "SELECT DISTINCT u FROM User u " +
            "left JOIN FETCH u.roles r ")
    List<User> findAllUsers();

    @Query(value = "SELECT distinct u from User u left join fetch u.annexes where u.id = ?1")
    Optional<User> findByIdWithAnnexes(long userId);



}
