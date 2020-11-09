package com.softlines.fastpos.validationTestExamples;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.controller.AnnexController;
import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.UserController;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.repository.AnnexRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, RoutingDatasourceTestProfileJPAConfig.class, TestSecurityJPAConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserValidationTestExample {
    @Autowired
    private MockMvc mvc;

    @Test
    public void userController_saveUser_validationTest_invalidPassword() throws Exception {
        var user = UserDTO.builder()
                .id(1l)
                .username("adminExample")
                .enabled(true)
                .build();

        mvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(jsonPath("password", is("validation.user.error.password")))
                .andExpect(status().isUnprocessableEntity()).andReturn();
    }
    @Test
    public void userController_saveUser_validationTest_invalidUsername() throws Exception {
        var user = UserDTO.builder()
                .id(1l)
                .password("passwordExample")
                .enabled(true)
                .build();

        mvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(jsonPath("username", is("validation.user.error.username")))
                .andExpect(status().isUnprocessableEntity()).andReturn();
    }
    @Test
    public void userController_saveUser_validationTest_nullEnabled() throws Exception {
        var user = UserDTO.builder()
                .id(1l)
                .password("passwordExample")
                .username("userExample")
                .build();

        mvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(jsonPath("enabled", is("must not be null")))
                .andExpect(status().isUnprocessableEntity()).andReturn();
    }
    @Test
    public void userController_saveUser_validationTest_invalidUsernameInvalidPasswordNullEnabled() throws Exception {
        var user = UserDTO.builder()
                .id(1l)
                .build();

        mvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(jsonPath("enabled", is("must not be null")))
                .andExpect(jsonPath("username", is("validation.user.error.username")))
                .andExpect(jsonPath("password", is("validation.user.error.password")))
                .andExpect(status().isUnprocessableEntity()).andReturn();
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
