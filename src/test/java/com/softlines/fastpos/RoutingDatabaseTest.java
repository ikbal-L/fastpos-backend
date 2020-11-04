package com.softlines.fastpos;

import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.dbconfig.configuration.CustomContextHolder;
import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.UserController;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.repository.AnnexRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;

import static  org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, RoutingDatasourceTestProfileJPAConfig.class, TestSecurityJPAConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoutingDatabaseTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;

    @Autowired
    private UserController userController;

    @Autowired
    private AnnexRepository annexRepository;

    @Test
    public void switchingUsersBetween2Databases() throws Exception {
        //arrange
        var admin = UserDTO.builder()
                .dbId(1l)
                .id(1l)
                .enabled(true)
                .username("admin")
                .password("admin").build();
        var user = UserDTO.builder()
                .dbId(2l)
                .id(2l)
                .enabled(true)
                .username("user")
                .password("user").build();

        userController.addUser(admin);
        userController.addUser(user);

        String adminToken = obtainAccessToken(admin.getUsername(), admin.getPassword());
        String userToken = obtainAccessToken(user.getUsername(), user.getPassword());

        CustomContextHolder.setId(admin.getDbId());
        Annex annex_db_admin = Annex.builder().name("annex DB Admin").build();
        annexRepository.save(annex_db_admin);

        CustomContextHolder.clear();
        CustomContextHolder.setId(user.getDbId());
        Annex annex_db_user = Annex.builder().name("annex DB user").build();
        annexRepository.save(annex_db_user);

        //act
        var annex1 = annexRepository.findAll().get(0);

        //assert
        assertEquals(annex1.getName(), annex_db_user.getName());

    }

    @Test
    public void obtainAccessTokenTest() throws Exception {
        var user = UserDTO.builder()
                .dbId(2l)
                .id(2l)
                .enabled(true)
                .username("user")
                .password("user").build();

        var user1 = userController.addUser(user);

        String adminToken = obtainAccessToken(user.getUsername(), user.getPassword());

    }

    private String obtainAccessToken(String username, String password) throws Exception {
        String content = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        var result = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                //.andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getHeader("Authorization");
    }

}
