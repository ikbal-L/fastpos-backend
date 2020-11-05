package com.softlines.fastpos.controllersUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.PrivilegeController;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.UserController;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.UserMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class UserControllerUnitTesting {
    @MockBean
    JWTuserRepository jwTuserRepository;
    @Autowired
    UserController userController;
    @Autowired
    private UserMapper userMapper;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    public void userController_saveUser_existingUser(){
        var user = JWTuser.builder()
                .username("testUser")
                .id(1l)
                .password(encoder.encode("password")).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.ofNullable(user));

        var userDTO = userMapper.toUserDto(user);

        var response = userController.addUser(userDTO);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void userController_saveUser_nonExistingUser(){
        var user = JWTuser.builder()
                .username("testUser")
                .id(1l)
                .password("password").build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.empty());
        Mockito.when(jwTuserRepository.save(Mockito.any(JWTuser.class))).thenReturn(user);
        var userDTO = userMapper.toUserDto(user);
        var response = userController.addUser(userDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    public void userController_saveUser_nullPassword(){
        var user = JWTuser.builder()
                .username("testUser")
                .id(1l).build();
        var response = userController.addUser(userMapper.toUserDto(user));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("no password provided", response.getBody());
    }
//    @Test
//    public void userController_saveUser_nullUsername(){
//        var user = JWTuser.builder()
//                .password("password")
//                .id(1l).build();
//        var response = userController.addUser(userMapper.toUserDto(user));
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
    @Test
    public void userController_saveUser_noConnection(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenThrow(DataAccessResourceFailureException.class);
        var response = userController.addUser(userMapper.toUserDto(user));
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void userController_getAllUsers_returnUsersList(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        var user2 = JWTuser.builder()
                .username("bbb")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(2l).name("ROLE_USER").build()))
                .id(2l).build();
        Mockito.when(jwTuserRepository.findAllUsers()).thenReturn(Arrays.asList(user, user2));
        var response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("aaa", response.getBody().get(0).getUsername());
        assertEquals("bbb", response.getBody().get(1).getUsername());
        assertEquals(1l, response.getBody().get(0).getRoleIds().get(0));
        assertEquals(2l, response.getBody().get(1).getRoleIds().get(0));
    }
    @Test
    public void userController_getAllUsers_returnEmptyList(){
        Mockito.when(jwTuserRepository.findAllUsers()).thenReturn(Arrays.asList());
        var response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
    @Test
    public void userController_getAllUsers_returnNull(){
        Mockito.when(jwTuserRepository.findAllUsers()).thenReturn(null);
        var response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void userController_getAllUsers_noConnection(){
        Mockito.when(jwTuserRepository.findAllUsers()).thenThrow(DataAccessResourceFailureException.class);
        var response = userController.getAllUsers();
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void userController_getUserByUsername_nonExistingUser(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findByUsername("aaa")).thenReturn(null);
        var response = userController.getUserByUsername("aaa");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void userController_getUserByUsername_ExistingUser(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findByUsername("aaa")).thenReturn(user);
        var response = userController.getUserByUsername("aaa");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userMapper.toUserDto(user), response.getBody());
    }
    @Test
    public void userController_getUserByUsername_noConnection(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findByUsername("aaa")).thenThrow(DataAccessResourceFailureException.class);
        var response = userController.getUserByUsername("aaa");
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void userController_deleteUser_nonExistingUser(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.empty());
        var userDTO = userMapper.toUserDto(user);
        var response = userController.deleteUserById(1l, userDTO);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void userController_deleteUser_ExistingUser(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.ofNullable(user));
        var userDTO = userMapper.toUserDto(user);
        var response = userController.deleteUserById(1l, userDTO);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }
    @Test
    public void userController_deleteUser_noConnection(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenThrow(DataAccessResourceFailureException.class);
        var userDTO = userMapper.toUserDto(user);
        var response = userController.deleteUserById(1l, userDTO);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void userController_putUser_nonExistingUser(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.empty());
        var userDTO = userMapper.toUserDto(user);
        var response = userController.editUserById(1l, userDTO);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void userController_putUser_ExistingUser(){
        var user = JWTuser.builder()
                .username("bbb")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        var userToEdit = JWTuser.builder()
                .username("aaa")
                .password(encoder.encode("password"))
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.ofNullable(userToEdit));
        var userDTO = userMapper.toUserDto(user);
        var response = userController.editUserById(1l, userDTO);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(1l, response.getBody().getId());
        assertEquals("bbb", response.getBody().getUsername());
    }
    @Test
    public void userController_putUser_noConnection(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(Role.builder().id(1l).name("ROLE_ADMIN").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenThrow(DataAccessResourceFailureException.class);
        var userDTO = userMapper.toUserDto(user);
        var response = userController.editUserById(1l, userDTO);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void userController_getRoles_nonExistingUser(){
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.empty());
        var response = userController.getUserRoles(1l);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void userController_getRoles_ExistingUser(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(
                        Role.builder().id(1l).name("ROLE_ADMIN").build(),
                        Role.builder().id(1l).name("ROLE_USER").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.ofNullable(user));
        var response = userController.getUserRoles(1l);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(2, response.getBody().size());
    }
    @Test
    public void userController_getRoles_noConnection(){
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(
                        Role.builder().id(1l).name("ROLE_ADMIN").build(),
                        Role.builder().id(1l).name("ROLE_USER").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenThrow(DataAccessResourceFailureException.class);
        var response = userController.getUserRoles(1l);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

    @Test
    public void userController_getPrivileges_nonExistingUser(){
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.empty());
        var response = userController.getUserPrivileges(1l);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void userController_getPrivileges_ExistingUser(){
        var privilege1 = Privilege.builder()
                .id(1l)
                .name("READ_PRIVILEGE").build();
        var privilege2 = Privilege.builder()
                .id(2l)
                .name("WRITE_PRIVILEGE").build();
        var user = JWTuser.builder()
                .username("aaa")
                .password("password")
                .roles(Arrays.asList(
                        Role.builder().id(1l).name("ROLE_ADMIN").privileges(Arrays.asList(privilege1, privilege2)).build(),
                        Role.builder().id(1l).name("ROLE_USER").build()))
                .id(1l).build();
        Mockito.when(jwTuserRepository.findById(1l)).thenReturn(Optional.ofNullable(user));
        var response = userController.getUserPrivileges(1l);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        //assertEquals(2, response.getBody().size());
    }
    @Test
    public void userController_getPrivileges_noConnection(){
        Mockito.when(jwTuserRepository.findById(1l)).thenThrow(DataAccessResourceFailureException.class);
        var response = userController.getUserRoles(1l);
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }

}
