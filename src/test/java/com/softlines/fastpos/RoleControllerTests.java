//package com.softlines.fastpos;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.softlines.fastpos.jwtsecurity.jwtcontroller.RoleController;
//import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
//import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
//import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
//import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
//import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
////import org.junit.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.server.ResponseStatusException;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
//@AutoConfigureMockMvc
//@TestMethodOrder(OrderAnnotation.class)
//public class RoleControllerTests {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    HttpServletResponse response;
//
//    @MockBean
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private RoleMapper roleMapper;
//
//    @Autowired
//    RoleController roleController;
//
//    RoleDTO roleDTO = new RoleDTO();
//
//    Role role = new Role();
//
//    @Test
//    @Order(1)
//    public void adminSavingRoleReturn202() throws Exception {
//        role.setName("finance");
//        mvc.perform(post("/role/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(role)))
//                .andDo(print())
//                .andExpect(jsonPath("name", is(roleRepository.findAll().get(2).getName())))
//                .andExpect(status().isCreated());
//    }
//    @Test
//    @Order(2)
//    public void adminSavivngExistingRoleReturn204() throws Exception {
//        role.setName("finance");
//        mvc.perform(delete("/role/delete")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(roleDTO)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//    @Test
//    @Order(3)
//    public void adminEditingRoleReturn200() throws Exception {
//        long id = roleRepository.findAll().get(2).getId();
//        roleDTO.setId(id);
//        roleDTO.setName("ROLE_HR");
//        mvc.perform(put("/role/put")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(roleDTO)))
//                .andDo(print())
//                .andExpect(jsonPath("name", is(roleRepository.findAll().get(2).getName())))
//                .andExpect(status().isOk());
//    }
//    @Test
//    @Order(4)
//    public void adminDeletingRoleReturn202() throws Exception {
//        long id = roleRepository.findAll().get(2).getId();
//        roleDTO.setId(id);
//        mvc.perform(delete("/role/delete")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(roleDTO)))
//                .andDo(print())
//                .andExpect(status().isAccepted());
//    }
//    @Test
//    @Order(5)
//    public void adminDeletingNonExistingRoleReturn204() throws Exception {
//        roleDTO.setId(0l);
//        mvc.perform(delete("/role/delete")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(roleDTO)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//    @Test
//    @Order(6)
//    public void adminEditingNonExistingRoleReturn204() throws Exception {
//        roleDTO.setId(0l);
//        mvc.perform(put("/role/put")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(roleDTO)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//    @Test
//    @Order(7)
//    public void adminGettingRolesMatchesDbReturn200() throws Exception {
//        List<RoleDTO> roleDTOS = roleMapper.toRoleDTOs(roleRepository.findAllRolesWithPrivileges());
//        mvc.perform(get("/role/getall")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$", hasSize(roleDTOS.size())))
//                .andExpect(jsonPath("$[0].name", is(roleDTOS.get(0).getName())))
//                .andExpect(jsonPath("$[0].privilegeIds[0]", is((roleDTOS.get(0).getPrivilegeIds().get(0)).intValue())))
//                .andExpect(jsonPath("$[0].privilegeIds", hasSize(roleDTOS.get(0).getPrivilegeIds().size())))
//                .andExpect(jsonPath("$[1].name", is(roleDTOS.get(1).getName())))
//                .andExpect(jsonPath("$[1].privilegeIds[0]", is((roleDTOS.get(1).getPrivilegeIds().get(0)).intValue())))
//                .andExpect(jsonPath("$[1].privilegeIds", hasSize(roleDTOS.get(1).getPrivilegeIds().size())))
//                .andExpect(status().isOk());
//    }
//    @Test
//    @Order(8)
//    public void adminGettingRoleByNameMatchesDbReturn200() throws Exception {
//        RoleDTO roleDTO = roleMapper.toRoleDto(roleRepository.findByName("ROLE_ADMIN"));
//        mvc.perform(get("/role/getbyname/{roleName}", "admin")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("name", is(roleDTO.getName())))
//                .andExpect(jsonPath("privilegeIds[0]", is((roleDTO.getPrivilegeIds().get(0)).intValue())))
//                .andExpect(jsonPath("privilegeIds", hasSize(roleDTO.getPrivilegeIds().size())))
//                .andExpect(status().isOk());
//    }
//    @Test
//    @Order(9)
//    public void adminGettingNonExistingRoleByNameReturn204() throws Exception {
//        mvc.perform(get("/role/getbyname/{roleName}", "admin0")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isNoContent());
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
//}
