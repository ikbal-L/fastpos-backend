package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.controller.AnnexController;
import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.UserController;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.repository.AnnexRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, RoutingDatasourceTestProfileJPAConfig.class, TestSecurityJPAConfig.class})
@AutoConfigureMockMvc
//@DataJpaTest
//@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
public class AnnexControllerTestExample {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;
    @Autowired
    AnnexRepository mockedAnnexRepository;

    @Autowired
    AnnexRepository annexRepository;
    @Autowired
    JWTuserRepository userRepository;

    @Autowired
    AnnexController annexController;
    @Autowired
    UserController userController;

    @LocalServerPort
    private int port;

    //Annex annex = new Annex();
    //you should never use a class variable
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void getAllAnnexes_AnnexesListNotEmpty_NotMocked_usingTestRestTemplate() throws Exception {
        var user = UserDTO.builder()
                .username("admin")
                .enabled(true)
                .password("admin").build();

        userController.addUser(user);

        var annex = Annex.builder()
                .name("abc")
                .address("adre123")
                .serverLicenceKey("ket123")
                .build();
        var saved = annexRepository.save(new Annex(3, "annex1", "addr", "key123"));
        var saved2 = annexRepository.save(annex);
        var annexes = annexRepository.findAll();
        TestRestTemplate testRestTemplate
                = new TestRestTemplate(user.getUsername(), user.getPassword());
        //createUser("admin", "admin");
        var token = obtainAccessToken("admin", "admin");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token);

        ResponseEntity<String> annexResp =
                testRestTemplate.exchange(createURLWithPort("/annex/getall"),
                        HttpMethod.GET, new HttpEntity<>(null, headers), String.class);// (Class<List<Annex>>)(Object)List.class);

        mvc.perform(get("/annex/getall")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(annexes.size()))))
                .andExpect(jsonPath("$[0].address").value(annexes.get(0).getAddress()));
    }

    @Test
    public void getAllAnnexes_AnnexesListNotEmpty_NotMocked() throws Exception {


        var annex = Annex.builder()
                .name("abc")
                .address("adre123")
                .serverLicenceKey("ket123")
                .build();
        var saved = annexRepository.save(new Annex(3, "annex1", "addr", "key123"));
        var saved2 = annexRepository.save(annex);
        var annexes = annexRepository.findAll();

        MvcResult res = mvc.perform(get("/annex/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(annexes.size()))))
                .andExpect(jsonPath("$[0].address").value(annexes.get(0).getAddress()))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    public void getAllAnnexes_AnnexesListNotEmpty() throws Exception {
        List<Annex> annexesList = new ArrayList<>();
        annexesList.add(new Annex(1, "aaa", "adr", "key"));
        Mockito.when(mockedAnnexRepository.findAll()).thenReturn(annexesList);

        var annexes = mockedAnnexRepository.findAll();

        var res = mvc.perform(get("/annex/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(annexes.size()))))
                .andExpect(jsonPath("$[0].address").value(annexes.get(0).getAddress()))
                .andExpect(status().isOk())
                .andReturn();
        var resp = res.getResponse();
    }

    @Test
    public void getAllAnnexes_AnnexesListNotEmpty_Unit() throws Exception {
        List<Annex> annexesList = new ArrayList<>();
        annexesList.add(new Annex(1, "aaa", "adr", "key"));
        Mockito.when(annexRepository.findAll()).thenReturn(annexesList);

        var res = annexController.getAnnexs();

        Assertions.assertEquals(res.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(((List<Annex>) res.getBody()).get(0).getName(), "aaa");
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

    private void createUser(String username, String password) throws Exception {
        String content = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\", \"enabled\": true}";
        var result = mvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        result.getResponse().getHeaderNames();
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        String content = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        var result = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getHeader("Authorization");
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
