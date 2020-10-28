package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import org.aspectj.weaver.ast.Var;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Order;
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

import static com.softlines.fastpos.jwtsecurity.securityfilters.JWTAuthenticationFilter.createToken;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
public class RoleControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    HttpServletResponse response;

    Role role = new Role();

    Privilege privilege = new Privilege();

    @Test
    @Order(0)
    public void userAddingRolesReturn403() throws Exception {

        role.setName("finance");
        //adding
        mvc.perform(post("/role/save")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(role)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(1)
    public void userDeletingRolesByIdReturn403() throws Exception {
        //deleting by id
        mvc.perform(delete("/role/deletebyid/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(2)
    public void userDeletingRolesByNameReturn403() throws Exception {
        //deleting by name
        mvc.perform(delete("/role/deletebyname/{name}", "read")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(3)
    public void userAddingPrivilegeToRoleReturn403() throws Exception {
        //adding privilege to role
        privilege.setName("update");
        mvc.perform(put("/role/addprivilege/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(4)
    public void userRemovingPrivilegeFromRoleReturn403() throws Exception {
        //removing privilege from role
        privilege.setName("read");
        mvc.perform(delete("/role/removeprivilege/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(5)
    public void userEditingRolesReturn403() throws Exception {
        role.setName("finance");
        //editing
        mvc.perform(put("/role/edit/{id}", 7)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(role)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(6)
    public void userShowingRoleByIdReturn403() throws Exception {
        //show a role by id
        mvc.perform(get("/role/getById/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @Order(7)
    public void userShowingAllRolesReturn403() throws Exception {
        //show all roles
        mvc.perform(get("/role/getall")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    public void adminAddingRolesReturn201() throws Exception {
        role.setName("finance");
        //adding
        mvc.perform(post("/role/save")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(role)))
                .andDo(print())
                .andExpect(jsonPath("name", is("ROLE_FINANCE")))
                .andExpect(status().isCreated());
    }
    @Test
    @Order(9)
    public void adminDeletingRolesByNameReturn202() throws Exception {
        //deleting by name
        mvc.perform(delete("/role/deletebyname/{name}", "finance")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name", is("ROLE_FINANCE")))
                .andExpect(status().isAccepted());
    }
    @Test
    @Order(10)
    public void adminAddingPrivilegeToRoleReturn202() throws Exception {
        //adding privilege to role
        privilege.setName("update");
        mvc.perform(put("/role/addprivilege/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(jsonPath("privilegeIds").value(Matchers.containsInAnyOrder(4, 5, 9)))
                .andExpect(status().isAccepted());
    }
    @Test
    @Order(11)
    public void adminRemovingPrivilegeFromRoleReturn202() throws Exception {
        //removing privilege from role
        privilege.setName("update");
        mvc.perform(delete("/role/removeprivilege/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(jsonPath("privilegeIds").value(Matchers.containsInAnyOrder(4, 5)))
                .andExpect(status().isAccepted());
    }
    @Test
    @Order(12)
    public void adminEditingRolesReturn200() throws Exception {
        role.setName("hr");
        //editing
        mvc.perform(put("/role/edit/{id}", 7)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(role)))
                .andDo(print())
                .andExpect(jsonPath("name", is("ROLE_HR")))
                .andExpect(status().isOk());
        role.setName("user");
        //editing back
        mvc.perform(put("/role/edit/{id}", 7)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(role)))
                .andDo(print())
                .andExpect(jsonPath("name", is("ROLE_USER")))
                .andExpect(status().isOk());
    }
    @Test
    @Order(13)
    public void adminShowingRoleByIdReturn200() throws Exception {
        //show a role by id
        mvc.perform(get("/role/getById/{id}", 6)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name", is("ROLE_ADMIN")))
                .andExpect(status().isOk());
    }
    @Test
    @Order(14)
    public void adminShowingAllRolesReturn200() throws Exception {
        //show all roles
        mvc.perform(get("/role/getall")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].name", is("ROLE_ADMIN")))
                .andExpect(jsonPath("$[1].name", is("ROLE_USER")))
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
