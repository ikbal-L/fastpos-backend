package com.softlines.fastpos.security.securityrepository;

import com.softlines.fastpos.security.securitydomain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

}
