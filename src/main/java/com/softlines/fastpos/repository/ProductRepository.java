package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.repository.em.CustomOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomOrderRepository {

    @Query(value = "select distinct p from Product p  LEFT JOIN FETCH  p.additives WHERE p.name = ?1")
    List<Product> findByName(String name);

    @Query(value = "select distinct p from Product p LEFT JOIN FETCH  p.additives")
    List<Product> findAllProductsWithAdditives();

    @Query(value = "select distinct p from Product p LEFT JOIN FETCH  p.additives WHERE p.id = ?1")
    Product findByIdProductWithAdditives(long id);

    @Query(value = "select distinct p from Product p LEFT JOIN FETCH  p.additives where p.id IN :ids")
    List<Product> findManyProductsWithAdditives(@Param("ids") List<Long> ProductIds);



}
