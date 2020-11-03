package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.ProductController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class ProductControllerUnitTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductController productController ;

    @Test
    public void productController_getAll_WithNotEmptyProductsList() throws Exception {

        var products = Arrays.asList(
                Product.builder()
                        .id(1l)
                        .name("Pizza")
                        .additives(Arrays.asList(Additive.builder().id(1).description("Harrisa").build()))
                        .category(Category.builder().build())
                        .build()
        );

        Mockito.when(productRepository.findAll()).thenReturn(products);

        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getName(), products.get(0).getName());
        assertEquals(((List<ProductDto>) res.getBody()).size(), 1);
        assertEquals(((List<ProductDto>) res.getBody()).get(0).getIdAdditives().get(0), products.get(0).getAdditives().get(0).getId());
        assertEquals(((List<ProductDto>) res.getBody()).get(0).getIdAdditives().size(), 1);

          }

    @Test
    public void productController_getAll_WithEmptyProductsList() throws Exception {
        var Products = new ArrayList<Product>();
        Mockito.when(productRepository.findAll()).thenReturn(Products);
        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void productController_getAll_WithNullProductsList() throws Exception {
        Mockito.when(productRepository.findAll()).thenReturn(null);

        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void productController_gelAll_getProductsWithNoDBConnectionException() throws Exception {
        Mockito.when(productRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProducts();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

}
