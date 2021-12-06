package com.softlines.fastpos.mappingUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.security.controllers.UserController;
import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Role;

import com.softlines.fastpos.security.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.security.securitydomain.securitymapper.UserMapper;
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

    User user1, user2;
    Role role1, role2;
    UserDTO userDTO1, userDTO2;
    List<User> users;
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
        user1 = User.builder()
                .id(1l)
                .username("testAdmin")
                .password(("password"))
                .build();
        user2 = User.builder()
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
        user1.setRoles(null);
        user2.setRoles(null);
        userDTO1.setRoleIds(null);
        userDTO2.setRoleIds(null);
    }

    //User to DTO: Single user
    @Test
    public void userMapper_UserToDTO_rolesExist(){
        user1.setRoles(Arrays.asList(role1, role2));
        var userDTO = userMapper.toUserDto(user1);
        assertEquals(user1.getId(), userDTO.getId());
        assertEquals(user1.getUsername(), userDTO.getUsername());
        assertEquals(user1.getPassword(), userDTO.getPassword());
        assertEquals(user1.getRoles().size(), userDTO.getRoleIds().size());
    }
    @Test
    public void userMapper_UserToDTO_emptyRolesList(){
        user1.setRoles(Arrays.asList());
        var userDTO = userMapper.toUserDto(user1);
        assertEquals(user1.getId(), userDTO.getId());
        assertEquals(user1.getUsername(), userDTO.getUsername());
        assertEquals(user1.getPassword(), userDTO.getPassword());
        assertEquals(user1.getRoles().size(), userDTO.getRoleIds().size());
    }
    @Test
    public void userMapper_UserToDTO_nullRolesList(){
        user1.setRoles(null);
        var userDTO = userMapper.toUserDto(user1);
        assertEquals(user1.getId(), userDTO.getId());
        assertEquals(user1.getUsername(), userDTO.getUsername());
        assertEquals(user1.getPassword(), userDTO.getPassword());
        assertEquals(user1.getRoles(), userDTO.getRoleIds());
    }

    //User to DTO: list of users
    @Test
    public void userMapper_ListUserssToListUserDTOs_rolesExist(){
        user1.setRoles(Arrays.asList(role1, role2));
        user2.setRoles(Arrays.asList(role1));
        users = Arrays.asList(user1, user2);
        var userDtos = userMapper.toUserDTOs(users);
        assertEquals(users.size(), userDtos.size());
        assertEquals(users.get(0).getUsername(), userDtos.get(0).getUsername());
        assertEquals(users.get(0).getPassword(), userDtos.get(0).getPassword());
        assertEquals(users.get(0).getRoles().size(), userDtos.get(0).getRoleIds().size());
        assertEquals(users.get(1).getUsername(), userDtos.get(1).getUsername());
        assertEquals(users.get(1).getPassword(), userDtos.get(1).getPassword());
        assertEquals(users.get(1).getRoles().size(), userDtos.get(1).getRoleIds().size());
    }
    @Test
    public void userMapper_ListUserssToListUserDTOs_emptyRolesList(){
        user1.setRoles(Arrays.asList());
        user2.setRoles(Arrays.asList());
        users = Arrays.asList(user1, user2);
        var userDtos = userMapper.toUserDTOs(users);
        assertEquals(users.size(), userDtos.size());
        assertEquals(users.get(0).getUsername(), userDtos.get(0).getUsername());
        assertEquals(users.get(0).getPassword(), userDtos.get(0).getPassword());
        assertEquals(users.get(0).getRoles().size(), userDtos.get(0).getRoleIds().size());
        assertEquals(users.get(1).getUsername(), userDtos.get(1).getUsername());
        assertEquals(users.get(1).getPassword(), userDtos.get(1).getPassword());
        assertEquals(users.get(1).getRoles().size(), userDtos.get(1).getRoleIds().size());
    }
    @Test
    public void userMapper_ListUserssToListUserDTOs_nullRolesList(){
        user1.setRoles(null);
        user2.setRoles(null);
        users = Arrays.asList(user1, user2);
        var userDtos = userMapper.toUserDTOs(users);
        assertEquals(users.size(), userDtos.size());
        assertEquals(users.get(0).getUsername(), userDtos.get(0).getUsername());
        assertEquals(users.get(0).getPassword(), userDtos.get(0).getPassword());
        assertEquals(users.get(0).getRoles(), userDtos.get(0).getRoleIds());
        assertEquals(users.get(1).getUsername(), userDtos.get(1).getUsername());
        assertEquals(users.get(1).getPassword(), userDtos.get(1).getPassword());
        assertEquals(users.get(1).getRoles(), userDtos.get(1).getRoleIds());
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
