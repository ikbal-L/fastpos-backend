package com.softlines.fastpos.controllersUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.RoleController;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.junit.jupiter.api.Test;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class RoleControllerUnitTesting {

    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    RoleController roleController;
    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void RoleController_getRoles_ReturnsNotEmptyRolesList() {
        var roles = Arrays.asList(
                Role.builder().id(1l).name("ROLE_HR")
                        .privileges(Arrays.asList(Privilege.builder().id(1l).name("privilege01").build()))
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
    public void RoleController_getRoles_ReturnsEmptyRolesList() {
        var roles = new ArrayList<Role>();
        Mockito.when(roleRepository.findAllRolesWithPrivileges()).thenReturn(roles);

        var res = roleController.getRoles();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void RoleController_getRoles_withNullRolesList() {
        //arrange
        Mockito.when(roleRepository.findAllRolesWithPrivileges()).thenReturn(null);

        //act
        var res = roleController.getRoles();

        //assert
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void RoleController_getRolesWithNoDBConnection_Return502() {
        Mockito.when(roleRepository.findAllRolesWithPrivileges())
                .thenThrow(DataAccessResourceFailureException.class);
        var res = roleController.getRoles();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

    @Test
    public void RoleController_saveRole_saveNoEmptyRole() {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .id(1l)
                                        .build()))
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        role.setName("ROLE_HR");
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);
        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals(res.getBody().getName(), "ROLE_HR");
    }
    @Test
    public void RoleController_saveRole_saveExistingRole() {
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .id(1l)
                                        .build()))
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(role.getId())).thenReturn(Optional.of(role));

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    @Test
    public void RoleController_saveRole_WithNoConnection(){
        var role = Role.builder()
                                .id(1l).name("hr")
                                .privileges(Arrays.asList(Privilege.builder()
                                        .id(1l)
                                        .build()))
                                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(role.getId())).thenThrow(DataAccessResourceFailureException.class);

        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }
    @Test
    public void RoleController_saveRole_WithEmptyPrivileges() {
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
    public void RoleController_saveRole_WithNullPrivileges() {
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
    public void RoleController_saveRole_WithNullName() {
        var role = Role.builder()
                .id(1l)
                .privileges(null)
                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);
        var res = roleController.saveRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void RoleController_deleteRole_nonExistingRole() {
        var role = Role.builder().build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(roleDTO.getId())).thenReturn(Optional.empty());
        var res = roleController.deleteRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void RoleController_deleteRole_ExistingRole() {
        var role = Role.builder()
                .id(1l).name("hr")
                .privileges(null)
                .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(roleDTO.getId())).thenReturn(java.util.Optional.ofNullable(role));
        var res = roleController.deleteRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), roleDTO);
    }

    @Test
    public void RoleController_deleteRole_withNoConnection() {
    var role = Role.builder()
            .id(1l).name("hr")
            .privileges(null)
            .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(roleDTO.getId())).thenThrow(DataAccessResourceFailureException.class);
        var res = roleController.deleteRole(roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

    @Test
    public void RoleController_putRole_ExistingRole() {
        var role = Role.builder()
            .id(1l).name("ROLE_FINANCE")
            .privileges(null)
            .build();
        var roleDTO = roleMapper.toRoleDto(role);
        var roleToEdit = Role.builder()
                .id(1l).name("ROLE_HR")
                .privileges(null)
                .build();
        Mockito.when(roleRepository.findRoleById(roleDTO.getId())).thenReturn(java.util.Optional.ofNullable(roleToEdit));
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);
        var res = roleController.editRole(roleDTO.getId(), roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().getName(), "ROLE_FINANCE");
    }

    @Test
    public void RoleController_putRole_nonExistingRole() {
        var role = Role.builder()
            .id(1l).name("hr")
            .privileges(null)
            .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(roleDTO.getId())).thenReturn(Optional.empty());
        var res = roleController.editRole(roleDTO.getId(),roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void RoleController_putRole_withNoConnection() {
        var role = Role.builder()
            .id(1l).name("hr")
            .privileges(null)
            .build();
        var roleDTO = roleMapper.toRoleDto(role);
        Mockito.when(roleRepository.findRoleById(roleDTO.getId())).thenThrow(DataAccessResourceFailureException.class);
        var res = roleController.editRole(roleDTO.getId(),roleDTO);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

    @Test
    public void RoleController_getRoleByName_withNoConnection() {
        var roleName = "ROLE_HR";
        Mockito.when(roleRepository.findByName(roleName)).thenThrow(DataAccessResourceFailureException.class);
        var res = roleController.getRoleByName("ROLE_HR");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

    @Test
    public void RoleController_getRoleByName_nonExistingRole() {
        var roleName = "ROLE_HR";
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(null);
        var res = roleController.getRoleByName("ROLE_HR");
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void RoleController_getRoleByName_ExistingRole() {
        var roleName = "ROLE_HR";
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(Role.builder().id(1l).name("ROLE_HR").build());
        var res = roleController.getRoleByName(roleName);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().getName(), "ROLE_HR");
    }

    @Test
    public void RoleController_getRoleByName_nullOrEmptyRoleName() {
        var roleName = "";
        var res = roleController.getRoleByName(null);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
        var res2 = roleController.getRoleByName(roleName);
        assertEquals(res2.getStatusCode(), HttpStatus.NO_CONTENT);

    }


}
