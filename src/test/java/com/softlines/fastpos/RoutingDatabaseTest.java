package com.softlines.fastpos;

import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.security.controllers.UserController;
import com.softlines.fastpos.security.securityrepository.AnnexRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

    @Autowired
    @Qualifier("testingCustomRoutingDataSource")
    DataSource dataSource;

    @Value("classpath:sql/schema.sql")
    Resource createSchemaResourceFile;

    @Value("classpath:sql/dropschema.sql")
    Resource dropSchemaResourceFile;

    private void initDB() throws SQLException, IOException {
        FileSystemResource rc = new FileSystemResource(createSchemaResourceFile.getFile());
        EncodedResource encodeRes = new EncodedResource(rc, "GBK");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), encodeRes);
    }

    private void dropDB() throws SQLException, IOException {
        FileSystemResource rc = new FileSystemResource(dropSchemaResourceFile.getFile());
        EncodedResource encodeRes = new EncodedResource(rc, "GBK");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), encodeRes);
    }

    @BeforeEach
    public void initiateDB() throws IOException, SQLException {
        initDB();
    }
    @AfterEach
    public void clearDB() throws IOException, SQLException {
        dropDB();
    }

//    @Test
//    public void switchingUsersBetween2Databases() throws Exception {
//        //arrange
//        var admin = UserDTO.builder()
//                .id(1l)
//                .enabled(true)
//                .username("admin")
//                .password("admin").build();
//        var user = UserDTO.builder()
//                .id(2l)
//                .enabled(true)
//                .username("user")
//                .password("user").build();
//
//        userController.addUser(admin);
//        userController.addUser(user);
//
//        String adminToken = obtainAccessToken(admin.getUsername(), admin.getPassword());
//        String userToken = obtainAccessToken(user.getUsername(), user.getPassword());
//
//        CustomContextHolder.setId(admin.getDbId());
//        Annex annex_db_admin = Annex.builder().name("annex DB Admin").build();
//        annexRepository.save(annex_db_admin);
//
//        //act on db admin
//        var annex1 = annexRepository.findAll().get(0);
//
//        //assert on db admin
//        assertEquals(annex1.getName(), annex_db_admin.getName());
//
//        CustomContextHolder.clear();
//        CustomContextHolder.setId(user.getDbId());
//
//        Annex annex_db_user = Annex.builder().name("annex DB user").build();
//        annexRepository.save(annex_db_user);
//
//        //act on db user
//        var annex2 = annexRepository.findAll().get(0);
//
//        //assert on db user
//        assertEquals(annex2.getName(), annex_db_user.getName());
//        assertNotEquals(annex2.getName(), annex1.getName());
//    }

//    @Test
//    public void userHasNonExistingIdOfDbInfo(){
//        var admin = UserDTO.builder()
//                .dbId(3l)
//                .id(1l)
//                .enabled(true)
//                .username("admin")
//                .password("admin").build();
//        var response = userController.addUser(admin);
////        CustomContextHolder.clear();
////        CustomContextHolder.setId(admin.getDbId());
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }

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
