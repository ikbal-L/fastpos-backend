package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.dto.ProductDto;
import com.softlines.fastpos.dto.mapping.ProductMapper;
import com.softlines.fastpos.repository.ProductRepository;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;


    ProductDto product = new ProductDto();
    List<Long> listAdditives = new ArrayList();

    @Test
    public void getProducts() throws Exception {

        var products = productRepository.findAll();
        mvc.perform(get("/product/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[1].idAdditives[0]").value(productMapper.toProductDTOs(products).get(1).getIdAdditives().get(0)))
                .andExpect(status().isOk());

    }

    @Test
    public void getProduct() throws Exception {
        var product = productRepository.findById((long) 1).get();
        mvc.perform(get("/product/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name").value(productMapper.toProductDto(product).getName()))
                .andExpect(status().isOk());

    }


    @Test
    public void addProducts() throws Exception {

        product.setAvailableStock(0);
        product.setBackgroundString("red");
        product.setDescription("desc");
        product.setMuchInDemand(true);
        product.setName("pro 2");
        product.setRank(15);
        product.setType("type");
        product.setPlatter(true);
        product.setPrice(450);
        product.setType("sad");
        product.setUnit("U");
        product.setCategoryId(1);

        listAdditives.add(1l);
        product.setIdAdditives(listAdditives);

        mvc.perform(post("/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andDo(print())
                .andExpect(jsonPath("name", is("pro 2")))
                .andExpect(status().isCreated());

    }

    @Test
    public void getProductWithIdNotExist() throws Exception {
        mvc.perform(get("/product/get/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void putProducts() throws Exception {

        product.setId(2);
        product.setAvailableStock(0);
        product.setBackgroundString("red");
        product.setDescription("desc");
        product.setMuchInDemand(true);
        product.setName("pro put");
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
                .andExpect(jsonPath("name", is("pro put")))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteProducts() throws Exception {

        mvc.perform(delete("/product/delete/{id}", "19")
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
