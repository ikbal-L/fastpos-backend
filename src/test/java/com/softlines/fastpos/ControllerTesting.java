package com.softlines.fastpos;

import com.softlines.fastpos.dbconfig.dbTestController.DbTestController;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DbTestController.class)
public class ControllerTesting {

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Autowired
    private JWTuserRepository jwTuserRepository;

    @MockBean
    @Autowired
    ProductRepository productRepository;

    @Test
    public void firstTest() throws Exception{
        JWTuser jwTuser = new JWTuser();
        jwTuser.setUsername("bob");

        List<JWTuser> allJwTusers = Arrays.asList(jwTuser);

        given(jwTuserRepository.findAll()).willReturn(allJwTusers);

        mvc.perform(get("/dbtest/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_STRING, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImRiSUQiOjEsImV4cCI6MTYwMzQ0NTQxM30.wMP6NfPVlIN2CQ30o_uaEGfB-OliI6o2kfA35TTWmHHeO8FZpKz5_DLOb0LFhx7lVrkkAbC-eKx1SzqwXpufBA"))

                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(jwTuser.getUsername())));

    }

    @Test
    public void secondTest() throws Exception{
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        JWTuser jwTuser = new JWTuser();
        jwTuser.setUsername("admin");
        jwTuser.setPassword(encoder.encode("admin"));

        //List<JWTuser> allJwTusers = Arrays.asList(jwTuser);

        given(jwTuserRepository.findById(0l).get()).willReturn(jwTuser);

        mvc.perform(get("/dbtest/getbyID/0")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_STRING, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImRiSUQiOjEsImV4cCI6MTYwMzQ0NTQxM30.wMP6NfPVlIN2CQ30o_uaEGfB-OliI6o2kfA35TTWmHHeO8FZpKz5_DLOb0LFhx7lVrkkAbC-eKx1SzqwXpufBA"))

                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(jwTuser.getUsername())))
                .andReturn();

    }
}
