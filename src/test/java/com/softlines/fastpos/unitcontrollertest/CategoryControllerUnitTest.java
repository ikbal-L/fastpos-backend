package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.CategoryController;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.mapping.CategoryMapper;
import com.softlines.fastpos.repository.CategoryRepository;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class CategoryControllerUnitTest {

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryController categoryController;

    @Autowired
    CategoryMapper categoryMapper;

    @Test
    @Order(1)
    public void categoryController_getAll_WithNotEmptyCategoriesList() {

        var categories = Arrays.asList(
                Category.builder()
                        .id(1l)
                        .name("Tacos")
                        .build()
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), categories.get(0).getName());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    @Order(2)
    public void categoryController_getAll_WithEmptyCategoriesList() {
        var Categories = new ArrayList<Category>();
        when(categoryRepository.findAll()).thenReturn(Categories);
        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(4)
    public void categoryController_getAll_WithNullCategoriesList() {
        when(categoryRepository.findAll()).thenReturn(null);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    @Order(5)
    public void categoryController_getAll_getCategoriesWithNoDBConnection() {
        when(categoryRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategories();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Category Unit Test  <------------------------
     */


    @Test
    public void categoryController_Save_WithData() {

        var category =
                Category.builder()
                        .name("tacos")
                        .backgroundString("red")
                        .rank(2)

                        .products(Arrays.asList(Product.builder().id(1).build())).build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        var res = categoryController.addCategory(categoryMapper.toCategoryDto(category));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()), categoryMapper.toCategoryDto(category));

    }

    @Test
    public void categoryController_Save_WithExistCategory() {

        var category =
                Category.builder()
                        .id(1)
                        .name("tacos")
                        .backgroundString("red")
                        .rank(2)
                        .products(Arrays.asList(Product.builder().id(1).build())).build();

        when(categoryRepository.findById(category.getId())).thenReturn(java.util.Optional.of(category));
        var res = categoryController.addCategory(categoryMapper.toCategoryDto(category));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    public void categoryController_Save_WithoutData() {

        var category = Category.builder().build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        var res = categoryController.addCategory(categoryMapper.toCategoryDto(category));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);

    }


    @Test
    public void categoryController_save_WithNoDBConnection() {

        var category =
                Category.builder()
                        .id(1l)
                        .name("harrisa")
                        .backgroundString("red")
                        .rank(2)
                        .build();

        when(categoryRepository.save(any(Category.class))).thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.addCategory(categoryMapper.toCategoryDto(category));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Category Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void CategoryController_getById_WithNotEmptyCategory() {

        var category =
                Category.builder()
                        .id(1)
                        .rank(5)
                        .name("tacos")
                        .build();

        when(categoryRepository.findById(1l)).thenReturn(java.util.Optional.ofNullable(category));

        var res = categoryController.getCategory(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), category.getName());
    }

    @Test
    @Order(10)
    public void CategoryController_getById_WithEmptyCategory() {

        var Category = new Category();
        when(categoryRepository.findById(0l)).thenReturn(java.util.Optional.of(Category));
        var res = categoryController.getCategory(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void CategoryController_getById_WithNullCategory() {

        var res = categoryController.getCategory(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void CategoryController_getById_getCategoryWithNoDBConnection() {

        when(categoryRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategory(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }

}
