package com.softlines.fastpos.repository;

import com.softlines.fastpos.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<Category> findByName(String name);

}
