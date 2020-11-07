package com.softlines.fastpos.validationtest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.controller.ProductController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.repository.AdditiveRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, RoutingDatasourceTestProfileJPAConfig.class, TestSecurityJPAConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ValidationUnitTestingExampleIT {

    @Autowired
    ProductController productController;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AdditiveRepository additiveRepository;

    @Test
    public void NotValidAdditiveSavingTest(){

        var savedAdditive1 = additiveRepository.save(Additive.builder().id(3l).description("something").build());

        //ConstraintViolationException happens here
        //var savedAdditive12 = additiveRepository.save(Additive.builder().id(4l).build());

        var ex = assertThrows(ConstraintViolationException.class, ()->additiveRepository.save(Additive.builder().id(4l).build()));
        assertThat(ex.getConstraintViolations().size(), equalTo(1));
        assertThat(((ConstraintViolation)ex.getConstraintViolations().toArray()[0]).getMessage(),
                equalTo("Additive description should have a value"));
    }

    @Test
    public void savingProductWithAdditivesContainingOnlyIds(){

        var additive01 = Additive.builder().backgroundString("abc").description("def").build();
        var additive02 = Additive.builder().backgroundString("abc").description("def1").build();

        additiveRepository.save(additive01);
        additiveRepository.save(additive02);

        //must be added manually in DB
        var additive1 = additiveRepository.findById(1l).get();
        var additive2 = additiveRepository.findById(2l).get();


        var product = Product.builder()
                .description("111")
                .name("product 1")
                .price(100)
                .additives(Arrays.asList(additive1, additive2))
                .build();
        var product2 = Product.builder()
                .description("222")
                .name("product 2")
                .price(200)
                .additives(Arrays.asList(additive2))
                .build();
        var savedProduct2 = productRepository.save(product2);
        var savedProduct= productRepository.save(product);
        assertThat(savedProduct.getAdditives().size(), is(2));
        assertThat(savedProduct2.getAdditives().size(), is(1));
    }

}
