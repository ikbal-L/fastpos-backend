package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.configuration.H2TestProfileJPAConfig;
import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.repository.AnnexRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, H2TestProfileJPAConfig.class})
@AutoConfigureMockMvc
//@DataJpaTest
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
public class AnnexControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;
    @Autowired
    AnnexRepository mockedAnnexRepository;

    @Autowired
    AnnexRepository annexRepository;

    //Annex annex = new Annex();
    //you should never use a class variable

    @Test
    public void getAllAnnexes_AnnexesListNotEmpty_NotMocked() throws Exception {

        var annexes = annexRepository.findAll();
        var saved = annexRepository.save(new Annex(3, "annex1", "addr", "key123"));
        mvc.perform(get("/annex/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(annexes.size()))))
                .andExpect(jsonPath("$[0].address").value(annexes.get(0).getAddress()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllAnnexes_AnnexesListNotEmpty() throws Exception {
        List<Annex> annexesList = new ArrayList<>();
        annexesList.add(new Annex(1, "aaa", "adr","key"));
        Mockito.when(mockedAnnexRepository.findAll()).thenReturn(annexesList);

        var annexes = mockedAnnexRepository.findAll();

        mvc.perform(get("/annex/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(annexes.size()))))
                .andExpect(jsonPath("$[0].address").value(annexes.get(0).getAddress()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllAnnexes_AnnexesListIsEmpty() throws Exception {
        Mockito.when(mockedAnnexRepository.findAll()).thenReturn(new ArrayList<>());

        var annexes = mockedAnnexRepository.findAll();

        mvc.perform(get("/annex/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(annexes.size()))))
                .andExpect(status().isOk());
    }

    @Test
    public void getAnnex_ExistingOne() throws Exception {
        Mockito.when(mockedAnnexRepository.findById((long) 1))
                .thenReturn(Optional.of(new Annex(1, "aaa", "addr", "key123")));
        var optinalAnnex = mockedAnnexRepository.findById((long) 1);
        mvc.perform(get("/annex/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name").value(optinalAnnex.get().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAnnex_When_IdNotExist() throws Exception {
        Mockito.when(mockedAnnexRepository.findById((long) 5))
                .thenReturn(Optional.empty());
        var annex = mockedAnnexRepository.findById((long) 5);
        Assertions.assertEquals(false, annex.isPresent());
        mvc.perform(get("/annex/get/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveAnnex_with_notNullValue() throws Exception {
        Annex annex = new Annex();
        annex.setAddress("algeria");
        annex.setName("annex name");
        annex.setServerLicenceKey("0655-5054-0584-4054");

        mvc.perform(post("/annex/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(annex)))
                .andDo(print())
                .andExpect(jsonPath("name", is(annex.getName())))
                .andExpect(status().isCreated());

    }


    @Test
    public void putAnnex() throws Exception {

        Annex annex = new Annex();
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
