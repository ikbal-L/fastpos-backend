package com.softlines.fastpos.unitcontrollertest;


import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.ProductController;
import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public void productController_getAll_WithNotEmptyProductsList() throws Exception {

        var products = Arrays.asList(
                Product.builder()
                        .id(1L)
                        .name("Pizza")
                        .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
                        .category(Category.builder().build())
                        .build()
        );

        when(productRepository.findAllProductsWithAdditives()).thenReturn((List<Product>) products);

        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getName(), products.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
        assertEquals((res.getBody()).get(0).getIdAdditives().get(0), products.get(0).getAdditives().get(0).getId());
        assertEquals((res.getBody()).get(0).getIdAdditives().size(), 1);

    }

    @Test
    public void productController_getAll_WithEmptyProductsList() {
        var Products = new ArrayList<Product>();
        when(productRepository.findAllProductsWithAdditives()).thenReturn(Products);
        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void productController_getAll_WithNullProductsList() {
        when(productRepository.findAllProductsWithAdditives()).thenReturn(null);

        var res = productController.getProducts();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void productController_gelAll_getProductsWithNoDBConnectionException() {

        when(productRepository.findAllProductsWithAdditives())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProducts();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Get Many Product Unit Test  <------------------------
     */

    @Test
    public void productController_getMany_WithNotEmptyProductsList() {

        List<Product> products = Arrays.asList(
                Product.builder()
                        .id(1l)
                        .name("Pizza")
                        .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
                        .category(Category.builder().id(14).build())
                        .build()
        );
        List<Long> listIds = new ArrayList<>();
        listIds.add(1l);

        when(productRepository.findManyProductsWithAdditives(listIds)).thenReturn(products);
        var res = productController.getMany(listIds);


        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getName(), products.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
        assertEquals((res.getBody()).get(0).getIdAdditives().get(0), products.get(0).getAdditives().get(0).getId());
        assertEquals((res.getBody()).get(0).getIdAdditives().size(), 1);

    }

    @Test
    public void productController_getMany_WithEmptyProductsList() {
        var Products = new ArrayList<Product>();

        when(productRepository.findManyProductsWithAdditives(null)).thenReturn(Products);
        var res = productController.getMany(null);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void productController_getMany_WithNullProductsList() {
        when(productRepository.findAllProductsWithAdditives()).thenReturn(null);

        var res = productController.getMany(null);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void productController_getMany_getProductsWithNoDBConnectionException() {
        var products = Arrays.asList(
                Product.builder()
                        .id(1l)
                        .name("Pizza")
                        .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
                        .category(Category.builder().id(14).build())
                        .build()
        );
        List<Long> listIds = new ArrayList<>();
        listIds.add(1l);
        when(productRepository.findManyProductsWithAdditives(listIds))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getMany(listIds);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Save Product Unit Test  <------------------------
     */

    @Test
    public void productController_Save_WithData() {

        var product = Product.builder()
                .name("Pizza")
                .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
                .category(Category.builder().build())
                .build();

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        var res = productController.addProduct(productMapper.toProductDto(product));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);

    }


    @Test
    public void productController_Save_WithExistProduct() {

        var product = Product.builder()
                .id(2L)
                .name("tacos")
                .category(Category.builder().build())
                .additives(Arrays.asList(Additive.builder().build())).build();

        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        var res = productController.addProduct(productMapper.toProductDto(product));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    public void productController_save_WithNoDBConnection() {

        var product =
                Product.builder()
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
    public void ProductController_getById_WithNotEmptyAdditive() {

        var product =
                Product.builder()
                        .id(1)
                        .rank(5)
                        .description("harrisa")
                        .additives(Arrays.asList(Additive.builder().build()))
                        .category(Category.builder().build())
                        .build();

        when(productRepository.findByIdProductWithAdditives(1l)).thenReturn(product);

        var res = productController.getProduct(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getDescription(), product.getDescription());
    }


    @Test
    public void productController_getById_WithEmptyProduct() {

        var additive = new Product();
        when(productRepository.findByIdProductWithAdditives(0l)).thenReturn(additive);
        var res = productController.getProduct(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void productController_getById_WithNullProduct() {

        var res = productController.getProduct(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void additiveController_getById_WithNoDBConnection() {

        when(productRepository.findByIdProductWithAdditives(5l))
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

        when(productRepository.findByIdProductWithAdditives(1l)).thenReturn(product);
        productController.deleteProduct(1);

        verify(productRepository, times(1)).delete(product);


    }

    @Test
    public void productController_Delete_WithNotExistProductId() {

        when(productRepository.findByIdProductWithAdditives(1l)).thenReturn(null);

        productController.deleteProduct(1);

        verify(productRepository, times(1)).findByIdProductWithAdditives(1l);
        verifyNoMoreInteractions(productRepository);

    }

    @Test
    public void productController_Delete_WithNoDBConnection() {

        when(productRepository.findByIdProductWithAdditives(5l))
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
                .id(2l)
                .name("harrisa")
                .additives(Arrays.asList(Additive.builder().build()))
                .category(Category.builder().id(1).build())
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        ResponseEntity<ProductDto> returned = productController.editProduct(2, productMapper.toProductDto(product));

        verify(productRepository, times(1)).findById(product.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void productController_Put_WithIdNotExist() {

        var product = Product.builder()
                .name("harrisa")
                .category(Category.builder().build())
                .additives(Arrays.asList(Additive.builder().build()))
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
        ResponseEntity<ProductDto> returned = productController.editProduct(0, productMapper.toProductDto(product));

        verify(productRepository, times(1)).findById(product.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void productController_Put_WithNoDBConnection() {

        when(productRepository.findByIdProductWithAdditives(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = productController.getProduct(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}
