package com.softlines.fastpos.securityControllersIntegrationTests;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.configuration.RoutingDatasourceTestProfileJPAConfig;
import com.softlines.fastpos.configuration.TestSecurityJPAConfig;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.testStatics.Statics;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ModelApplication.class, RoutingDatasourceTestProfileJPAConfig.class, TestSecurityJPAConfig.class})
@AutoConfigureMockMvc
//@Profile("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivilegeControllerIntegrationTest {

    Privilege privilege;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    private MockMvc mvc;
    String adminToken;

    @BeforeEach
    public void getAccess() throws Exception {
        //adminToken = Statics.obtainAccessToken("admin", "admin", mvc);
    }

//    @Test
//    @Order(1)
//    public void privilegeController_savePrivilege_emptyName() throws Exception {
//        privilege = Privilege.builder()
//                .name("").build();
//        mvc.perform(post("/privilege/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Statics.asJsonString(privilege)))
//                //.header("Authorization", adminToken))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
    @Test
    @Order(1)
    public void privilegeController_savePrivilege_notEmptyName() throws Exception {
        privilege = Privilege.builder()
                .name("update").build();
        mvc.perform(post("/privilege/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is("UPDATE_PRIVILEGE")));
        privilegeRepository.delete(privilegeRepository.findByName("UPDATE_PRIVILEGE"));
    }
    @Test
    @Order(2)
    public void privilegeController_savePrivilege_nullName() throws Exception {
        privilege = Privilege.builder().build();
        mvc.perform(post("/privilege/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void privilegeController_deletePrivilege_nullPrivilege() throws Exception {
        privilege = null;
        mvc.perform(delete("/privilege/delete/{privilegeId}", 0l)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(4)
    public void privilegeController_deletePrivilege_nonExistingPrivilege() throws Exception {
        privilege = Privilege.builder()
                .id(1l).name("UPDATE_PRIVILEGE").build();
        mvc.perform(delete("/privilege/delete/{privilegeId}", 0l)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @Order(5)
    public void privilegeController_deletePrivilege_existingPrivilege() throws Exception {
        privilege = privilegeRepository.save(Privilege.builder()
                    .name("UPDATE_PRIVILEGE").build());
        mvc.perform(delete("/privilege/delete/{privilegeId}", privilege.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("name", is("UPDATE_PRIVILEGE")));
    }

    @Test
    @Order(6)
    public void privilegeController_putPrivilege_nullPrivilege() throws Exception {
        privilege = null;
        mvc.perform(put("/privilege/put/{privilegeId}", 0l)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(7)
    public void privilegeController_putPrivilege_nonExistingPrivilege() throws Exception {
        privilege = Privilege.builder()
                .id(1l).name("UPDATE_PRIVILEGE").build();
        mvc.perform(put("/privilege/put/{privilegeId}", 0l)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @Order(8)
    public void privilegeController_putPrivilege_existingPrivilege() throws Exception {
        privilege = privilegeRepository.save(Privilege.builder()
                    .name("UPDATE_PRIVILEGE").build());
        privilege.setName("EDIT");
        mvc.perform(put("/privilege/put/{privilegeId}", privilege.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Statics.asJsonString(privilege)))
                //.header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("EDIT_PRIVILEGE")));
        privilegeRepository.delete(privilege);
    }


}
