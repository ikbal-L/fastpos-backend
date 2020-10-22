package com.softlines.fastpos.dbconfig.dbTestController;

import com.softlines.fastpos.dbconfig.configuration.CustomContextHolder;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("dbtest")
public class DbTestController {

    @Autowired
    @Qualifier("customRoutingDataSource")
    DataSource dataSource;

    @Autowired
    ProductRepository productRepository;

    @PreAuthorize("@apiAuth.checkGrants(authentication, 'WRITE_PRIVILEGE')")
    @RequestMapping("/")
    public String testWithoutPreAuthorize(){

        String name = productRepository.findAll().get(0).getName();
        return name;

    }

}
