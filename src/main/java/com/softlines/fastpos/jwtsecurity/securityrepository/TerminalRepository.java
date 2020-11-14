package com.softlines.fastpos.repository;

import com.softlines.fastpos.jwtsecurity.securitydomain.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

}
