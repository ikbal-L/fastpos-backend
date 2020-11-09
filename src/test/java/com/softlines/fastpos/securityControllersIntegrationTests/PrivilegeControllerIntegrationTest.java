package com.softlines.fastpos.securityControllersIntegrationTests;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, RoutingDatasourceTestProfileJPAConfig.class, TestSecurityJPAConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivilegeControllerIntegrationTest {

    Privilege privilege;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    public void privilegeController_savePrivilege_emptyName() throws Exception {
        privilege = Privilege.builder()
                .name("").build();
        mvc.perform(post("/privilege/save", privilege).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("_PRIVILEGE", is(privilege.getName())));
    }
}
