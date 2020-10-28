package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    //@Transactional("transactionManager")
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        products.forEach(p -> p.getAdditives());
        return products;
    }

}
