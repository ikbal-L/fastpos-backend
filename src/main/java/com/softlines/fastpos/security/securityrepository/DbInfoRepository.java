package com.softlines.fastpos.security.securityrepository;

import com.softlines.fastpos.security.securitydomain.DbInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DbInfoRepository extends JpaRepository<DbInfo, Long> {
    @Query(value = "SELECT * FROM Dbinfo WHERE Dbinfo.name = ?1", nativeQuery = true)
    DbInfo findByName(String defaultDB);

    @Query(value = "SELECT * FROM Dbinfo WHERE Dbinfo.id = ?1", nativeQuery = true)
    DbInfo findByDbInfoId(Long id);
}
