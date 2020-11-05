package com.softlines.fastpos.lazyfetchTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.RoleController;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class RoleLazyFetchingTest {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    Role role;
    Privilege privilege1, privilege2;

    @BeforeEach
    public void initialize(){
        privilege1 = Privilege.builder()
                .id(1l)
                .name("UPDATE_PRIVILEGE")
                .build();
        privilege1 = privilegeRepository.save(privilege1);
        privilege2 = Privilege.builder()
                .id(2l)
                .name("EDIT_PRIVILEGE")
                .build();
        privilege2 = privilegeRepository.save(privilege2);
        role = Role.builder()
                .id(1l)
                .name("ROLE_HR")
                .privileges(Arrays.asList(privilege1, privilege2))
                .build();
        role = roleRepository.save(role);
    }
    @AfterEach
    public void clear(){
        roleRepository.delete(role);
        privilegeRepository.delete(privilege1);
        privilegeRepository.delete(privilege2);
    }

    @Test
    public void roleRepository_findRoleByName_fetchPrivileges(){
        Role role1 = roleRepository.findByName("ROLE_HR");
        assertEquals(2, role1.getPrivileges().size());
        assertEquals("UPDATE_PRIVILEGE", role1.getPrivileges().get(0).getName());
        assertEquals("EDIT_PRIVILEGE", role1.getPrivileges().get(1).getName());
    }

    @Test
    public void roleRepository_findRoleById_fetchPrivileges(){
        Role role1 = roleRepository.findById(role.getId()).get();
        assertEquals(2, role1.getPrivileges().size());
        assertEquals("UPDATE_PRIVILEGE", role1.getPrivileges().get(0).getName());
        assertEquals("EDIT_PRIVILEGE", role1.getPrivileges().get(1).getName());
    }

}
