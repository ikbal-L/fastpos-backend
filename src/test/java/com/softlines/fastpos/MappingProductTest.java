package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.dto.ProductDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Order;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class MappingProductTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    HttpServletResponse response;

    @Test
    public void getProducts() throws Exception {

        mvc.perform(get("/product/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].idAdditives").value(1))
                .andExpect(status().isOk());

    }

    @Test
    public void getProduct() throws Exception {

        mvc.perform(get("/product/get/{id}",2)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("idAdditives").value(Matchers.containsInAnyOrder(1,2)))
                .andExpect(status().isOk());

    }
    ProductDto product = new ProductDto();
    List<Long> listAdditives = new ArrayList();


    @Test
    public void addProducts() throws Exception {

        product.setAvailableStock(0);
        product.setBackgroundString("red");
        product.setDescription("desc");
        product.setMuchInDemand(true);
        product.setName("From Test");
        product.setRank(4);
        product.setPlatter(true);
        product.setPrice(30);
        product.setType("sad");
        product.setUnit("U");
        listAdditives.add((long) 2);
        listAdditives.add((long) 1);
        product.setCategoryId(1);
        product.setIdAdditives(listAdditives);

        mvc.perform(post("/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andDo(print())
                .andExpect(jsonPath("name", is("From Test")))
                .andExpect(status().isCreated());

    }


    @Test
    public void putProducts() throws Exception {

        product.setId(2);
        product.setAvailableStock(0);
        product.setBackgroundString("red");
        product.setDescription("desc");
        product.setMuchInDemand(true);
        product.setName("From Test");
        product.setRank(4);
        product.setPlatter(true);
        product.setPrice(30);
        product.setType("sad");
        product.setUnit("U");
        listAdditives.add((long) 2);
        listAdditives.add((long) 1);
        product.setCategoryId(1);
        product.setIdAdditives(listAdditives);

        mvc.perform(put("/product/put/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andDo(print())
                .andExpect(jsonPath("name", is("From Test")))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteProducts() throws Exception {

        mvc.perform(delete("/product/delete/{id}", "7")
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
