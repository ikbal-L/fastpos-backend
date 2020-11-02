package com.softlines.fastpos.controllersUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.RoleController;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleControllerUnitTesting {

    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    RoleController roleController;
    @Autowired
    private RoleMapper roleMapper;

    @Test
    @Order(1)
    public void RoleController_getRoles_ReturnsNotEmptyRolesList() throws Exception {
        var roles = Arrays.asList(
                Role.builder().id(1l).name("ROLE_HR")
                        .privileges(Arrays.asList(new Privilege(1l, "privilege01")))
                        .build());
        Mockito.when(roleRepository.findAllRolesWithPrivileges()).thenReturn(roles);

        var res = roleController.getRoles();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(((List<RoleDTO>) res.getBody()).get(0).getName(), roles.get(0).getName());
        assertEquals(((List<RoleDTO>) res.getBody()).size(), 1);
        assertEquals(((List<RoleDTO>) res.getBody()).get(0).getPrivilegeIds().size(), 1);
        assertEquals(((List<RoleDTO>) res.getBody()).get(0).getPrivilegeIds().get(0), roles.get(0).getId());
    }

    @Test
    @Order(2)
    public void RoleController_getRoles_ReturnsEmptyRolesList() throws Exception {
        var roles = new ArrayList<Role>();
        Mockito.when(roleRepository.findAllRolesWithPrivileges()).thenReturn(roles);

        var res = roleController.getRoles();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void RoleController_getRoles_ReturnsNullRolesList() throws Exception {
        Mockito.when(roleRepository.findAllRolesWithPrivileges()).thenReturn(null);

        var res = roleController.getRoles();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(4)
    public void RoleController_getRolesWithNoDBConnection_Return502() throws Exception {
        Mockito.when(roleRepository.findAllRolesWithPrivileges())
                .thenThrow(DataAccessResourceFailureException.class);
        var res = roleController.getRoles();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

    @Test
    @Order(5)
    public void RoleController_saveRole_ReturnsRoleDTOWithStatus201() throws Exception {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .id(1l)
                                        .build()))
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
    }
    @Test
    @Order(6)
    public void RoleController_saveExistingRole_Returns204() throws Exception {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .id(1l)
                                        .build()))
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findByName("ROLE_HR")).thenReturn(role);

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    @Test
    @Order(7)
    public void RoleController_saveRoleWithNoConnection_Returns502() throws Exception {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .id(1l)
                                        .build()))
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findByName("ROLE_HR")).thenThrow(DataAccessResourceFailureException.class);

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }
    @Test
    @Order(8)
    public void RoleController_saveRoleWithEmptyPrivileges_Returns201() throws Exception {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(new ArrayList<Privilege>())
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
    }
    @Test
    @Order(9)
    public void RoleController_saveRoleWithNullPrivileges_Returns201() throws Exception {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(null)
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
    }
    @Test
    @Order(10)
    public void RoleController_saveRoleWithNullName_Returns400() throws Exception {
        var role = Role.builder().build();
        var roleDTO = roleMapper.toRoleDto(role);
        //Mockito.when(roleRepository.save(role)).thenReturn(role);
        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


}
