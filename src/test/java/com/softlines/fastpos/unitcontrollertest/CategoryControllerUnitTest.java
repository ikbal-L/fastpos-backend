package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.CategoryController;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.repository.CategoryRepository;
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
public class CategoryControllerUnitTest {

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryController categoryController;

    @Test
    public void categoryController_ReturnsNotEmptCategoriesList() throws Exception {

        var categories = Arrays.asList(
                Category.builder()
                        .id(1l)
                        .name("Tacos")
                        .build());

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(((List<CategoryDto>) res.getBody()).get(0).getName(), categories.get(0).getName());
        assertEquals(((List<CategoryDto>) res.getBody()).size(), 1);

    }

    @Test
    public void categoryController_ReturnsEmptyCategoriesList() throws Exception {
        var Categories = new ArrayList<Category>();
        Mockito.when(categoryRepository.findAll()).thenReturn(Categories);
        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void categoryController_ReturnsNullCategorysList() throws Exception {
        Mockito.when(categoryRepository.findAll()).thenReturn(null);

        var res = categoryController.getCategories();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void categoryController_getCategoriesWithNoDBConnection_Return502() throws Exception {
        Mockito.when(categoryRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = categoryController.getCategories();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

}
