package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    
    @Query(value="select distinct t from Table t  WHERE t.number = ?1")
    Table findByNumberWithOrders(int Number);

    @Query(value="select distinct t from Table t ")
    List<Table> findAllTablesWithTableOrders();

    @Query(value="select distinct t from Table t WHERE t.id = ?1")
    Table findByIdTablesOrders(long id);

    @Query(value="select distinct t from Table t where t.id IN :ids")
    List<Table> getManyTablesWithOrders(@Param("ids") List<Long> TablesIds);

}
