package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Person;
import com.softlines.fastpos.domain.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    @Query(value="select w from Waiter w WHERE w.active = ?1")
    List<Waiter>  findAllActiveWaiters(boolean active);

}
