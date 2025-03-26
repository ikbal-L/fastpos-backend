package com.softlines.fastpos.validationtest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.controller.ProductController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.repository.AdditiveRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

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
        var ex = assertThrows(ConstraintViolationException.class, ()->additiveRepository.save(Additive.builder().id(4l).build()));
        assertThat(ex.getConstraintViolations().size(), equalTo(2));
        assertThat(((ConstraintViolation)ex.getConstraintViolations().toArray()[0]).getMessage(),
                equalTo("Additive background should have a value"));
        assertThat(((ConstraintViolation)ex.getConstraintViolations().toArray()[1]).getMessage(),
                equalTo("Additive description should have a value"));
    }

    @Test
    public void ValidAdditiveSavingTest(){

        Additive additiveToSave = Additive.builder().description("something").backgroundString("#ffaa11").build();
        var savedAdditive = additiveRepository.save(additiveToSave);
        assertThat(savedAdditive).isNotNull();
        assertThat(savedAdditive.getDescription()).isEqualTo(additiveToSave.getDescription());
    }

    @Test
    public void savingProduct_WithInvalidAdditivesList(){

        var additive01 = Additive.builder().backgroundString("abc").description("def").build();
        var additive02 = Additive.builder().backgroundString("abc").description("def1").build();

        additiveRepository.save(additive01);
        additiveRepository.save(additive02);

        var additive1 = additiveRepository.findById(1l);
        var additive2 = additiveRepository.findById(2l);

        assertThat(additive1.isPresent()).isTrue();
        assertThat(additive2.isPresent()).isTrue();

        var product = Product.builder()
                .description("111")
                .name("product 1")
                .price(100)
                .additives(Arrays.asList(Additive.builder().id(1l).build(), Additive.builder().id(2l).build()))
                .build();
        var savedProduct = productRepository.save(product);

        assertThat(savedProduct.getAdditives().size(), is(2));
    }

    @Test
    public void savingProduct_WithValidAdditivesList(){

        //arrange
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
        var savedProduct = productRepository.save(product);

        assertThat(savedProduct.getAdditives().size(), is(2));
        assertThat(savedProduct2.getAdditives().size(), is(1));
    }

}
