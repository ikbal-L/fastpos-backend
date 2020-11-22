package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value="select distinct p from Category p LEFT JOIN FETCH  p.products")
    List<Category> findAllCategoriesWithProducts();

    @Query(value="select distinct p from Category p  LEFT JOIN FETCH  p.products WHERE p.id = ?1")
    Category findByIdCategoryWithProducts(long id);

    @Query(value="select distinct p from Category p  LEFT JOIN FETCH  p.products where p.id IN :ids")
    List<Category> findManyCategoriesWithProducts(@Param("ids") List<Long> CategoryIds);

}
