package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.CategoryController;
import com.softlines.fastpos.domain.Category;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.CategoryDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


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

        when(categoryRepository.findAllCategoriesWithProducts()).thenReturn(categories);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), categories.get(0).getName());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    @Order(2)
    public void categoryController_getAll_WithEmptyCategoriesList() {
        var Categories = new ArrayList<Category>();
        when(categoryRepository.findAllCategoriesWithProducts()).thenReturn(Categories);
        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void categoryController_getAll_WithNullCategoriesList() {
        when(categoryRepository.findAllCategoriesWithProducts()).thenReturn(null);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    @Order(4)
    public void categoryController_getAll_WithNoDBConnection() {
        when(categoryRepository.findAllCategoriesWithProducts())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategories();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }
    /**
     * ------------------>  Put Category Unit Test  <------------------------
     */



    @Test
    @Order(1)
    public void categoryController_getMany_WithNotEmptyCategoriesList() {

        var categories = Arrays.asList(
                Category.builder()
                        .id(1l)
                        .name("Tacos")
                        .build()
        );

        when(categoryRepository.findAllCategoriesWithProducts()).thenReturn(categories);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), categories.get(0).getName());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    @Order(2)
    public void categoryController_getMany_WithEmptyCategoriesList() {
        var Categories = new ArrayList<Category>();
        when(categoryRepository.findAllCategoriesWithProducts()).thenReturn(Categories);
        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void categoryController_getMany_WithNullCategoriesList() {
        when(categoryRepository.findAllCategoriesWithProducts()).thenReturn(null);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    @Order(4)
    public void categoryController_getMany_WithNoDBConnection() {
        when(categoryRepository.findAllCategoriesWithProducts())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategories();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }
    
    
    /**
     * ------------------>  Save Category Unit Test  <------------------------
     */


    @Test
    @Order(5)
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
    @Order(6)
    public void categoryController_Save_WithExistCategory() {

        var category =
                Category.builder()
                        .id(1)
                        .name("tacos")
                        .backgroundString("red")
                        .rank(2)
                        .products(Arrays.asList(Product.builder().id(1).build())).build();

        when(categoryRepository.findByIdCategoryWithProducts(category.getId())).thenReturn(category);
        var res = categoryController.addCategory(categoryMapper.toCategoryDto(category));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(7)
    public void categoryController_Save_WithoutData() {

        var category = Category.builder().build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        var res = categoryController.addCategory(categoryMapper.toCategoryDto(category));

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(8)
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

        when(categoryRepository.findByIdCategoryWithProducts(1l)).thenReturn(category);

        var res = categoryController.getCategory(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), category.getName());
    }

    @Test
    @Order(10)
    public void CategoryController_getById_WithEmptyCategory() {

        var category = new Category();
        when(categoryRepository.findByIdCategoryWithProducts(0l)).thenReturn(category);
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
    public void CategoryController_getById_WithNoDBConnection() {

        when(categoryRepository.findByIdCategoryWithProducts(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategory(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }



    /**
     * ------------------>  Delete Category Unit Test  <------------------------
     */

    @Test
    @Order(13)
    public void categoryController_Delete_WithCategoryId() {

        var category =
                Category.builder()
                        .id(1l)
                        .name("name")
                        .build();

        when(categoryRepository.findByIdCategoryWithProducts(1l)).thenReturn(category);
        categoryController.deleteCategory(1);

        verify(categoryRepository, times(1)).delete(category);


    }

    @Test
    @Order(14)
    public void categoryController_Delete_WithNotExistCategoryId() {

        when(categoryRepository.findByIdCategoryWithProducts(1l)).thenReturn(null);

        categoryController.deleteCategory(1);

        verify(categoryRepository, times(1)).findByIdCategoryWithProducts(1l);
        verifyNoMoreInteractions(categoryRepository);

    }

    @Test
    @Order(15)
    public void categoryController_Delete_WithNoDBConnection() {

        when(categoryRepository.findByIdCategoryWithProducts(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategory(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Category Unit Test  <------------------------
     */

    @Test
    @Order(16)
    public void categoryController_Put_WithData() {

        var category = Category.builder()
                .id(1l)
                .name("harrisa")
                .build();

        when(categoryRepository.findByIdCategoryWithProducts(category.getId())).thenReturn(category);
        ResponseEntity<CategoryDto> returned = categoryController.editCategory(1,categoryMapper.toCategoryDto( category));

        verify(categoryRepository, times(1)).findByIdCategoryWithProducts(category.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    @Order(17)
    public void categoryController_Put_WithIdNotExist() {

        var category = Category.builder()
                .id(10l)
                .name("harrisa")
                .build();

        when(categoryRepository.findByIdCategoryWithProducts(category.getId())).thenReturn(null);
        ResponseEntity<CategoryDto> returned = categoryController.editCategory(10, categoryMapper.toCategoryDto( category));

        verify(categoryRepository, times(1)).findByIdCategoryWithProducts(category.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(18)
    public void categoryController_Put_WithNullData() {

        var category = Category.builder().id(1).build();

        when(categoryRepository.findByIdCategoryWithProducts(category.getId())).thenReturn(category);
        var returned = categoryController.editCategory(1,categoryMapper.toCategoryDto(category) );

        verify(categoryRepository, times(1)).findByIdCategoryWithProducts(category.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(19)
    public void categoryController_Put_WithNoDBConnection() {

        when(categoryRepository.findByIdCategoryWithProducts(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategory(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}
