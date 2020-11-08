package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.ProductController;
import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.repository.ProductRepository;
import org.junit.Test;
import org.junit.jupiter.api.Order;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class ProductControllerUnitTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductController productController;

    @Autowired
    ProductMapper productMapper;

    @Test
    @Order(1)
    public void productController_getAll_WithNotEmptyProductsList() throws Exception {

        var products = Arrays.asList(
                Product.builder()
                        .id(1l)
                        .name("Pizza")
                        .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
                        .category(Category.builder().build())
                        .build()
        );

        when(productRepository.findAll()).thenReturn(products);

        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getName(), products.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
        assertEquals((res.getBody()).get(0).getIdAdditives().get(0), products.get(0).getAdditives().get(0).getId());
        assertEquals((res.getBody()).get(0).getIdAdditives().size(), 1);

    }

    @Test
    @Order(2)
    public void productController_getAll_WithEmptyProductsList() {
        var Products = new ArrayList<Product>();
        when(productRepository.findAll()).thenReturn(Products);
        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void productController_getAll_WithNullProductsList() {
        when(productRepository.findAll()).thenReturn(null);

        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(4)
    public void productController_gelAll_getProductsWithNoDBConnectionException() {

        when(productRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProducts();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Save Product Unit Test  <------------------------
     */

    @Test
    @Order(5)
    public void productController_Save_WithData() {

        var product = Product.builder()
                        .id(1l)
                        .name("Pizza")
                        .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
                        .category(Category.builder().build())
                        .build();

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        var res = productController.addProduct(productMapper.toProductDto(product));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()).getDescription(), product.getDescription());

    }


    @Test
    @Order(6)
    public void productController_Save_WithoutData() {

        var product = Product.builder().category(Category.builder().build()).build();

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        var res = productController.addProduct(productMapper.toProductDto(product));

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(5)
    public void productController_Save_WithExistProduct() {

        var product = Product.builder()
                .id(1)
                .name("tacos")
                .category(Category.builder().build())
                .additives(Arrays.asList(Additive.builder().build())).build();

        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        var res = productController.addProduct(productMapper.toProductDto(product) );

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(7)
    public void productController_save_WithNoDBConnection() {

        var product =
                Product.builder()
                        .id(1l)
                        .name("harrisa")
                        .backgroundString("red")
                        .rank(2)
                        .category(Category.builder().build())
                        .build();

        when(productRepository.save(Mockito.any(Product.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = productController.addProduct(productMapper.toProductDto(product));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Product Unit Test  <------------------------
     */

    @Test
    @Order(9)
    public void AdditiveController_getById_WithNotEmptyAdditive() {

        var product =
                Product.builder()
                        .id(1)
                        .rank(5)
                        .description("harrisa")
                        .additives(Arrays.asList(Additive.builder().build()))
                        .category(Category.builder().build())
                        .build();

        when(productRepository.findById(1l)).thenReturn(java.util.Optional.ofNullable(product));

        var res = productController.getProduct(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getDescription(), product.getDescription());
    }


    @Test
    @Order(10)
    public void productController_getById_WithEmptyProduct() {

        var additive = new Product();
        when(productRepository.findById(0l)).thenReturn(java.util.Optional.of(additive));
        var res = productController.getProduct(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void productController_getById_WithNullProduct() {

        var res = productController.getProduct(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void additiveController_getById_getAdditivesWithNoDBConnection() {

        when(productRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProduct(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }



    /**
     * ------------------>  Delete Product Unit Test  <------------------------
     */

    @Test
    public void productController_Delete_WithProductId() {

        var product =
                Product.builder()
                        .id(1l)
                        .name("name")
                        .build();

        when(productRepository.findById(1l)).thenReturn(Optional.ofNullable(product));
        productController.deleteProduct(1);

        verify(productRepository, times(1)).delete(product);


    }

    @Test
    public void productController_Delete_WithNotExistProductId() {

        when(productRepository.findById(1l)).thenReturn(null);

        productController.deleteProduct(1);

        verify(productRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(productRepository);

    }

    @Test
    @Order(12)
    public void productController_Delete_getProductesWithNoDBConnection() {

        when(productRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProduct(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Product Unit Test  <------------------------
     */

    @Test
    public void productController_Put_WithData() {

        var product = Product.builder()
                .id(1l)
                .name("harrisa")
                .category(Category.builder().build())
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        ResponseEntity<ProductDto> returned = productController.editProduct(1,productMapper.toProductDto( product));

        verify(productRepository, times(1)).findById(product.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void productController_Put_WithIdNotExist() {

        var product = Product.builder()
                .id(10l)
                .name("harrisa")
                .category(Category.builder().build())
                .additives(Arrays.asList(Additive.builder().build()))
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
        ResponseEntity<ProductDto> returned = productController.editProduct(10, productMapper.toProductDto( product));

        verify(productRepository, times(1)).findById(product.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void productController_Put_WithNullData() {

        var product = Product.builder()
                .id(1)
                .category(Category.builder().build())
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        ResponseEntity<ProductDto> returned = productController.editProduct(1, productMapper.toProductDto( product));

        verify(productRepository, times(1)).findById(product.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void productController_Put_getProductesWithNoDBConnection() {

        when(productRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProduct(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}
