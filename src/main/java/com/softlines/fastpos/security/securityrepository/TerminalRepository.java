package com.softlines.fastpos.security.securityrepository;

import com.softlines.fastpos.security.securitydomain.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

}
