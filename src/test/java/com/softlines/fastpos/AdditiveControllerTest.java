package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.hamcrest.Matchers;
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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AdditiveControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;
    @Autowired
    AdditiveRepository additiveRepository;

    AdditiveDto Additive = new AdditiveDto();

    @Test
    public void getAdditives() throws Exception {

        var additives = additiveRepository.findAll();

        mvc.perform(get("/additive/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(additives.size()))))
                .andExpect(jsonPath("$[0].description").value(additives.get(0).getDescription()))
                .andExpect(status().isOk());

    }

    @Test
    public void getAdditive() throws Exception {

        var additive = additiveRepository.findById((long) 1);

        mvc.perform(get("/additive/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("description").value(additive.get().getDescription()))
                .andExpect(status().isOk());

    }

    @Test
    public void getAdditiveWithIdNotExist() throws Exception {
        var additive = additiveRepository.findById((long) 1);
        mvc.perform(get("/additive/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void addAdditives() throws Exception {

        Additive.setBackgroundString("red");
        Additive.setRank(4);
        Additive.setDescription("harrisa+++");

        mvc.perform(post("/additive/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Additive)))
                .andDo(print())
                .andExpect(jsonPath("name", is("From Test")))
                .andExpect(status().isCreated());

    }


    @Test
    public void putAdditives() throws Exception {

        Additive.setId(1);
        Additive.setBackgroundString("red");
        Additive.setDescription("desc");
        Additive.setRank(4);


        mvc.perform(put("/additive/put/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Additive)))
                .andDo(print())
                .andExpect(jsonPath("name", is("From Test")))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteAdditives() throws Exception {

        mvc.perform(delete("/additive/delete/{id}", "1")
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
