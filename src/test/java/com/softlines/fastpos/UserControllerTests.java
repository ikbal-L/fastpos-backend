//package com.softlines.fastpos;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
//import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
//import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
//import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
//import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
//import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.UserMapper;
//import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
//import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
//@AutoConfigureMockMvc
//@TestMethodOrder(OrderAnnotation.class)
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
//    @Autowired
//    UserMapper userMapper;
//
//    Role role = new Role();
//    JWTuser user = new JWTuser();
//    UserDTO userDTO = new UserDTO();
//    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//    @Test
//    @Order(1)
//    public void addingNonExistingUserReturn202() throws Exception{
//        userDTO.setUsername("admin0");
//        userDTO.setPassword("admin0");
//        userDTO.setDbInfoId(3l);
//
//        mvc.perform(post("/user/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDTO)))
//                .andDo(print())
//                .andExpect(jsonPath("username", is(jwTuserRepository.findAll().get(2).getUsername())))
//                .andExpect(jsonPath("password", is(jwTuserRepository.findAll().get(2).getPassword())))
//                .andExpect(status().isCreated());
//    }
//    @Test
//    @Order(2)
//    public void addingExistingUserReturn404() throws Exception{
//        userDTO.setUsername("admin0");
//        userDTO.setPassword("admin0");
//        userDTO.setDbInfoId(3l);
//
//        mvc.perform(post("/user/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDTO)))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @Order(3)
//    public void deletingExistingUserReturn202() throws Exception{
//        userDTO = userMapper.toUserDto(jwTuserRepository.findAllUsers().get(2));
//
//        mvc.perform(delete("/user/delete/18")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDTO)))
//                .andDo(print())
//                .andExpect(status().isAccepted());
//    }
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
