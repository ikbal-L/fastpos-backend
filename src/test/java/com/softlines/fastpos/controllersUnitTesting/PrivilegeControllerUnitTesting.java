package com.softlines.fastpos.controllersUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.PrivilegeController;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class PrivilegeControllerUnitTesting {
    @MockBean
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PrivilegeController privilegeController;

    @Test
    public void privilegeController_savePrivilege_nonExistingPrivilege(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.save(Mockito.any(Privilege.class))).thenReturn(privilege);
        var response = privilegeController.addPrivilege(privilege);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(privilege, response.getBody());
    }
    @Test
    public void privilegeController_savePrivilege_ExistingPrivilege(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findByName("EDIT_PRIVILEGE")).thenReturn(privilege);
        var response = privilegeController.addPrivilege(privilege);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void privilegeController_savePrivilege_withNullPrivilegeName(){
        var privilege = Privilege.builder()
                .id(1l)
                .build();
        //Mockito.when(privilegeRepository.findByName("EDIT_PRIVILEGE")).thenReturn(privilege);
        var response = privilegeController.addPrivilege(privilege);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void privilegeController_savePrivilege_withNoConnection(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findByName("EDIT_PRIVILEGE")).thenThrow(DataAccessResourceFailureException.class);
        var response = privilegeController.addPrivilege(privilege);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void privilegeController_deletePrivilege_nonExistingPrivilege(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findById(privilege.getId())).thenReturn(Optional.empty());
        var response = privilegeController.deletePrivilege(privilege, 1l);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void privilegeController_deletePrivilege_ExistingPrivilege(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findById(privilege.getId())).thenReturn(Optional.of(privilege));
        var response = privilegeController.deletePrivilege(privilege, 1l);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(privilege, response.getBody());
    }
    @Test
    public void privilegeController_deletePrivilege_noConnection(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findById(privilege.getId())).thenThrow(DataAccessResourceFailureException.class);
        var response = privilegeController.deletePrivilege(privilege, 1l);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }
    @Test
    public void privilegeController_deletePrivilege_nullPrivilege(){
        var response = privilegeController.deletePrivilege(null, 1l);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void privilegeController_putPrivilege_nonExistingPrivilege(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findById(privilege.getId())).thenReturn(Optional.empty());
        var response = privilegeController.edit(1l, privilege);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void privilegeController_putPrivilege_ExistingPrivilege(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        var privilegeToEdit = Privilege.builder()
                .id(1l)
                .name("UPDATE")
                .build();
        Mockito.when(privilegeRepository.findById(privilege.getId())).thenReturn(Optional.of(privilegeToEdit));
        Mockito.when(privilegeRepository.save(Mockito.any(Privilege.class))).thenReturn(privilege);
        var response = privilegeController.edit(1l, privilege);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(privilege, response.getBody());
    }
    @Test
    public void privilegeController_putPrivilege_noConnection(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT")
                .build();
        Mockito.when(privilegeRepository.findById(privilege.getId())).thenThrow(DataAccessResourceFailureException.class);
        var response = privilegeController.edit(1l, privilege);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }
    @Test
    public void privilegeController_putPrivilege_nullPrivilege(){
        var response = privilegeController.edit(1l, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void privilegeController_getPrivileges(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT_PRIVILEGE")
                .build();
        var privilege2 = Privilege.builder()
                .id(2l)
                .name("UPDATE_PRIVILEGE")
                .build();
        Mockito.when(privilegeRepository.findAll()).thenReturn(Arrays.asList(privilege, privilege2));
        var response = privilegeController.getPrivileges();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("EDIT_PRIVILEGE", response.getBody().get(0).getName());
        assertEquals("UPDATE_PRIVILEGE", response.getBody().get(1).getName());
    }
    @Test
    public void privilegeController_getPrivileges_noConnection(){
        Mockito.when(privilegeRepository.findAll()).thenThrow(DataAccessResourceFailureException.class);
        var response = privilegeController.getPrivileges();
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void privilegeController_getPrivilegeById_nonExistingId(){
        Mockito.when(privilegeRepository.findById(0l)).thenReturn(Optional.empty());
        var response = privilegeController.getPrivilegeById(0l);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void privilegeController_getPrivilegeById_ExistingId(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT_PRIVILEGE")
                .build();
        Mockito.when(privilegeRepository.findById(1l)).thenReturn(Optional.ofNullable(privilege));
        var response = privilegeController.getPrivilegeById(1l);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("EDIT_PRIVILEGE", response.getBody().getName());
    }
    @Test
    public void privilegeController_getPrivilegeById_noConnection(){
        var privilege = Privilege.builder()
                .id(1l)
                .name("EDIT_PRIVILEGE")
                .build();
        Mockito.when(privilegeRepository.findById(1l)).thenThrow(DataAccessResourceFailureException.class);
        var response = privilegeController.getPrivilegeById(1l);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

}
