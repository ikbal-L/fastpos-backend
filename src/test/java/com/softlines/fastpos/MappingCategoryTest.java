package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class MappingCategoryTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;
    @Autowired
    CategoryRepository categoryRepository;

    CategoryDto categoryDto = new CategoryDto();

    @Test
    public void getcategorys() throws Exception {

        var categories = categoryRepository.findAll();

        mvc.perform(get("/category/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(categories.size()))))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).getName()))
                .andExpect(status().isOk());

    }

    @Test
    public void getCategory() throws Exception {
        var category = categoryRepository.findById((long) 1);

        mvc.perform(get("/category/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name").value(category.get().getName()))
                .andExpect(status().isOk());

    }

    @Test
    public void getCategoryWithIdNotExist() throws Exception {
        var category = categoryRepository.findById((long) 5);
        mvc.perform(get("/category/get/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void addCategory() throws Exception {

        categoryDto.setBackgroundString("red");
        categoryDto.setRank(4);
        categoryDto.setName("Boison");

        mvc.perform(post("/category/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(categoryDto)))
                .andDo(print())
                .andExpect(jsonPath("name", is("Boison")))
                .andExpect(status().isCreated());

    }


    @Test
    public void putCategory() throws Exception {

        categoryDto.setId(2L);
        categoryDto.setBackgroundString("red");
        categoryDto.setName("boison");
        categoryDto.setRank(4);

        mvc.perform(put("/category/put/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(categoryDto)))
                .andDo(print())
                .andExpect(jsonPath("name", is("From Test")))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteCategory() throws Exception {

        mvc.perform(delete("/category/delete/{id}", "3")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

    }

    public String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
