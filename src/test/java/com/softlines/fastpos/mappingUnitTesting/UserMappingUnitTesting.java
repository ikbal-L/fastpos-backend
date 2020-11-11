package com.softlines.fastpos.mappingUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.UserController;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;

import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class UserMappingUnitTesting {
    @Autowired
    UserController userController;
    @Autowired
    UserMapper userMapper;

    JWTuser jwTuser1, jwTuser2;
    Role role1, role2;
    UserDTO userDTO1, userDTO2;
    List<JWTuser> jwTusers;
    List<UserDTO> userDTOS;

    @BeforeEach
    public void initialize(){
        role1 = Role.builder()
                .id(1l)
                .name("ROLE_ADMIN")
                .build();
        role2 = Role.builder()
                .id(2l)
                .name("ROLE_USER")
                .build();
        jwTuser1 = JWTuser.builder()
                .id(1l)
                .username("testAdmin")
                .password(("password"))
                .build();
        jwTuser2 = JWTuser.builder()
                .id(2l)
                .username("testUser")
                .password(("password"))
                .build();
        userDTO1 = UserDTO.builder()
                .username("TestDTO1")
                .password("testPassword")
                .id(3l)
                .build();
        userDTO2 = UserDTO.builder()
                .username("TestDTO2")
                .password("testPassword")
                .id(4l)
                .build();
    }
    @AfterEach
    public void clear(){
        role1.setPrivileges(null);
        role2.setPrivileges(null);
        jwTuser1.setRoles(null);
        jwTuser2.setRoles(null);
        userDTO1.setRoleIds(null);
        userDTO2.setRoleIds(null);
    }

    //User to DTO: Single user
    @Test
    public void userMapper_UserToDTO_rolesExist(){
        jwTuser1.setRoles(Arrays.asList(role1, role2));
        var userDTO = userMapper.toUserDto(jwTuser1);
        assertEquals(jwTuser1.getId(), userDTO.getId());
        assertEquals(jwTuser1.getUsername(), userDTO.getUsername());
        assertEquals(jwTuser1.getPassword(), userDTO.getPassword());
        assertEquals(jwTuser1.getRoles().size(), userDTO.getRoleIds().size());
    }
    @Test
    public void userMapper_UserToDTO_emptyRolesList(){
        jwTuser1.setRoles(Arrays.asList());
        var userDTO = userMapper.toUserDto(jwTuser1);
        assertEquals(jwTuser1.getId(), userDTO.getId());
        assertEquals(jwTuser1.getUsername(), userDTO.getUsername());
        assertEquals(jwTuser1.getPassword(), userDTO.getPassword());
        assertEquals(jwTuser1.getRoles().size(), userDTO.getRoleIds().size());
    }
    @Test
    public void userMapper_UserToDTO_nullRolesList(){
        jwTuser1.setRoles(null);
        var userDTO = userMapper.toUserDto(jwTuser1);
        assertEquals(jwTuser1.getId(), userDTO.getId());
        assertEquals(jwTuser1.getUsername(), userDTO.getUsername());
        assertEquals(jwTuser1.getPassword(), userDTO.getPassword());
        assertEquals(jwTuser1.getRoles(), userDTO.getRoleIds());
    }

    //User to DTO: list of users
    @Test
    public void userMapper_ListUserssToListUserDTOs_rolesExist(){
        jwTuser1.setRoles(Arrays.asList(role1, role2));
        jwTuser2.setRoles(Arrays.asList(role1));
        jwTusers = Arrays.asList(jwTuser1, jwTuser2);
        var userDtos = userMapper.toUserDTOs(jwTusers);
        assertEquals(jwTusers.size(), userDtos.size());
        assertEquals(jwTusers.get(0).getUsername(), userDtos.get(0).getUsername());
        assertEquals(jwTusers.get(0).getPassword(), userDtos.get(0).getPassword());
        assertEquals(jwTusers.get(0).getRoles().size(), userDtos.get(0).getRoleIds().size());
        assertEquals(jwTusers.get(1).getUsername(), userDtos.get(1).getUsername());
        assertEquals(jwTusers.get(1).getPassword(), userDtos.get(1).getPassword());
        assertEquals(jwTusers.get(1).getRoles().size(), userDtos.get(1).getRoleIds().size());
    }
    @Test
    public void userMapper_ListUserssToListUserDTOs_emptyRolesList(){
        jwTuser1.setRoles(Arrays.asList());
        jwTuser2.setRoles(Arrays.asList());
        jwTusers = Arrays.asList(jwTuser1, jwTuser2);
        var userDtos = userMapper.toUserDTOs(jwTusers);
        assertEquals(jwTusers.size(), userDtos.size());
        assertEquals(jwTusers.get(0).getUsername(), userDtos.get(0).getUsername());
        assertEquals(jwTusers.get(0).getPassword(), userDtos.get(0).getPassword());
        assertEquals(jwTusers.get(0).getRoles().size(), userDtos.get(0).getRoleIds().size());
        assertEquals(jwTusers.get(1).getUsername(), userDtos.get(1).getUsername());
        assertEquals(jwTusers.get(1).getPassword(), userDtos.get(1).getPassword());
        assertEquals(jwTusers.get(1).getRoles().size(), userDtos.get(1).getRoleIds().size());
    }
    @Test
    public void userMapper_ListUserssToListUserDTOs_nullRolesList(){
        jwTuser1.setRoles(null);
        jwTuser2.setRoles(null);
        jwTusers = Arrays.asList(jwTuser1, jwTuser2);
        var userDtos = userMapper.toUserDTOs(jwTusers);
        assertEquals(jwTusers.size(), userDtos.size());
        assertEquals(jwTusers.get(0).getUsername(), userDtos.get(0).getUsername());
        assertEquals(jwTusers.get(0).getPassword(), userDtos.get(0).getPassword());
        assertEquals(jwTusers.get(0).getRoles(), userDtos.get(0).getRoleIds());
        assertEquals(jwTusers.get(1).getUsername(), userDtos.get(1).getUsername());
        assertEquals(jwTusers.get(1).getPassword(), userDtos.get(1).getPassword());
        assertEquals(jwTusers.get(1).getRoles(), userDtos.get(1).getRoleIds());
    }

    //User to DTO: single user
    @Test
    public void userMapper_DtoToUser_rolesIdsExist(){
        userDTO1.setRoleIds(Arrays.asList(1l, 2l));
        var user = userMapper.toJWTuser(userDTO1);
        assertEquals(userDTO1.getId(), user.getId());
        assertEquals(userDTO1.getUsername(), user.getUsername());
        assertEquals(userDTO1.getPassword(), user.getPassword());
        assertEquals(userDTO1.getRoleIds().size(), user.getRoles().size());
    }
    @Test
    public void userMapper_DtoToUser_emptyRolesIdsList(){
        userDTO1.setRoleIds(Arrays.asList());
        var user = userMapper.toJWTuser(userDTO1);
        assertEquals(userDTO1.getId(), user.getId());
        assertEquals(userDTO1.getUsername(), user.getUsername());
        assertEquals(userDTO1.getPassword(), user.getPassword());
        assertEquals(userDTO1.getRoleIds().size(), user.getRoles().size());
    }
    @Test
    public void userMapper_DtoToUser_nullRolesList(){
        userDTO1.setRoleIds(null);
        var user = userMapper.toJWTuser(userDTO1);
        assertEquals(userDTO1.getId(), user.getId());
        assertEquals(userDTO1.getUsername(), user.getUsername());
        assertEquals(userDTO1.getPassword(), user.getPassword());
        assertEquals(userDTO1.getRoleIds(), user.getRoles());
    }
}
