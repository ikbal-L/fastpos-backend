package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Tables;
import com.softlines.fastpos.domain.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {
    
    @Query(value="select distinct t from Tables t  LEFT JOIN FETCH  t.tableOrders WHERE t.number = ?1")
    Tables findByNumberWithOrders(int Number);

    @Query(value="select distinct t from Tables t LEFT JOIN FETCH  t.tableOrders")
    List<Tables> findAllTablesWithTableOrders();

    @Query(value="select distinct t from Tables t LEFT JOIN FETCH  t.tableOrders WHERE t.id = ?1")
    Tables findByIdTablesOrders(long id);

    @Query(value="select distinct t from Tables t LEFT JOIN FETCH  t.tableOrders where t.id IN :ids")
    List<Tables> getManyTablesWithOrders(@Param("ids") List<Long> TablesIds);

}
