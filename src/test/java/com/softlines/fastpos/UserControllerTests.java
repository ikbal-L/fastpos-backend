//package com.softlines.fastpos;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
//import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
//import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
//import org.hamcrest.Matchers;
//import org.junit.Test;
//import org.junit.jupiter.api.Order;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.servlet.http.HttpServletResponse;
//
//import static com.softlines.fastpos.jwtsecurity.securityfilters.JWTAuthenticationFilter.createToken;
//import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
//import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;
//import static org.hamcrest.CoreMatchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
//@AutoConfigureMockMvc
//@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
//public class UserControllerTests {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    HttpServletResponse response;
//
//    @Autowired
//    JWTuserRepository jwTuserRepository;
//
//    Role role = new Role();
//    JWTuser user = new JWTuser();
//
//    @Test
//    @Order(0)
//    public void userAddingUserReturn403() throws Exception {
//        user.setUsername("testuser");
//        user.setPassword("password");
//        //adding
//        mvc.perform(post("/user/save")
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(user)))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @Order(1)
//    public void userDeletingUserReturn403() throws Exception {
//        mvc.perform(delete("/user/delete/{id}", 7)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(2)
//    public void userEditingUserReturn403() throws Exception {
//        user.setUsername("testuser");
//        user.setPassword("password");
//        mvc.perform(put("/user/edit/{id}", 7)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(user)))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(3)
//    public void userAddingRoleToUserReturn403() throws Exception {
//        mvc.perform(put("/user/addrole/{userId}/{roleId}", 7, 24)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(4)
//    public void userRemovingRoleFromUserReturn403() throws Exception {
//        mvc.perform(delete("/user/removerole/{userId}/{roleId}", 7, 6)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(4)
//    public void userGettingUserRolesReturn403() throws Exception {
//        mvc.perform(get("/user/getroles/{userId}", 7)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(5)
//    public void userGettingUserPrivilegesReturn403() throws Exception {
//        mvc.perform(get("/user/getprivileges/{userId}", 7)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(6)
//    public void userAddingDbInfoToUsersReturn403() throws Exception {
//        mvc.perform(put("/user/adddbinfo/{userId}/{dbInfoId}", 7, 3)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @Order(7)
//    public void userRemovingDbInfoFromUsersReturn403() throws Exception {
//        mvc.perform(delete("/user/removedbinfo/{userId}/{dbInfoId}", 7, 1)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @Order(8)
//    public void adminAddingUserReturn201() throws Exception {
//        user.setUsername("testuser");
//        user.setPassword("password");
//        //adding
//        mvc.perform(post("/user/save")
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(user)))
//                .andDo(print())
//                .andExpect(jsonPath("username", is("testuser")))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @Order(9)
//    public void adminDeletingUserReturn202() throws Exception {
//        long id = jwTuserRepository.findByUsername("testuser").getId();
//        mvc.perform(delete("/user/delete/{id}", id)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("username", is("testuser")))
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(10)
//    public void adminEditingUserReturn202() throws Exception {
//        user.setUsername("testuser");
//        mvc.perform(put("/user/edit/{id}", 2)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(user)))
//                .andDo(print())
//                .andExpect(jsonPath("username", is("testuser")))
//                .andExpect(status().isAccepted());
//        user.setUsername("user");
//        mvc.perform(put("/user/edit/{id}", 2)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(user)))
//                .andDo(print())
//                .andExpect(jsonPath("username", is("user")))
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(11)
//    public void adminAddingAndRemovingRoleFromUserReturn202() throws Exception {
//        mvc.perform(put("/user/addrole/{userId}/{roleId}", 1, 7)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("roleIds").value(Matchers.containsInAnyOrder(6, 7)))
//                .andExpect(status().isAccepted());
//        mvc.perform(delete("/user/removerole/{userId}/{roleId}", 1, 7)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("roleIds").value(6l))
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(12)
//    public void adminGettingUserRolesReturn202() throws Exception {
//        mvc.perform(get("/user/getroles/{userId}", 1)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(13)
//    public void adminGettingUserPrivilegesReturn202() throws Exception {
//        mvc.perform(get("/user/getprivileges/{userId}", 1)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(14)
//    public void adminRemovingDbInfoFromUsersReturn202() throws Exception {
//        mvc.perform(delete("/user/removedbinfo/{userId}/{dbInfoId}", 1, 1)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(15)
//    public void adminAddingDbInfoToUsersReturn202() throws Exception {
//        mvc.perform(put("/user/adddbinfo/{userId}/{dbInfoId}", 1, 1)
//                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isAccepted());
//    }
//
//
//    public String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper mapper = new ObjectMapper();
//            return mapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
