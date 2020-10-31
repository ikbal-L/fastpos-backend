package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.repository.AnnexRepository;
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
public class AnnexControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;
    @Autowired
    AnnexRepository annexRepository;

    Annex annex = new Annex();

    @Test
    public void getAnnexes() throws Exception {

        var annexs = annexRepository.findAll();

        mvc.perform(get("/annex/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(annexs.size()))))
                .andExpect(jsonPath("$[0].address").value(annexs.get(0).getAddress()))
                .andExpect(status().isOk());

    }

    @Test
    public void getAnnex() throws Exception {

        var annex = annexRepository.findById((long) 1);

        mvc.perform(get("/annex/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name").value(annex.get().getName()))
                .andExpect(status().isOk());

    }

    @Test
    public void getAnnexWithIdNotExist() throws Exception {
        var annex = annexRepository.findById((long) 5);
        mvc.perform(get("/annex/get/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void addAnnex() throws Exception {

        annex.setAddress("algeria");
        annex.setName("annex name");
        annex.setServerLicenceKey("0655-5054-0584-4054");

        mvc.perform(post("/annex/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(annex)))
                .andDo(print())
                .andExpect(jsonPath("name", is("annex name")))
                .andExpect(status().isCreated());

    }


    @Test
    public void putAnnex() throws Exception {

        annex.setId(1);
        annex.setName("red");
        annex.setAddress("el oued");
        annex.setServerLicenceKey("9040-0841-0584-4054");

        mvc.perform(put("/annex/put/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(annex)))
                .andDo(print())
                .andExpect(jsonPath("address", is("el oued")))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteAnnex() throws Exception {

        mvc.perform(delete("/annex/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void deleteAnnexWithIdNotExist() throws Exception {

        mvc.perform(delete("/annex/delete/{id}", "20")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound());

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
