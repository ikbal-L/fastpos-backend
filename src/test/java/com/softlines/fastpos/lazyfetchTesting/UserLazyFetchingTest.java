package com.softlines.fastpos.lazyfetchTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Privilege;
import com.softlines.fastpos.security.securitydomain.Role;
import com.softlines.fastpos.security.securityrepository.UserRepository;
import com.softlines.fastpos.security.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.security.securityrepository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class UserLazyFetchingTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    User user;
    Role role1, role2;
    Privilege privilege1, privilege2;

    @BeforeEach
    public void startTest(){
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
        role1 = roleRepository.save(Role.builder()
                .name("ROLE_HR")
                .privileges(Arrays.asList(privilege1, privilege2))
                .build());
        role2 = roleRepository.save(Role.builder()
                .name("ROLE_FINANCE")
                .privileges(Arrays.asList(privilege1))
                .build());
        user = userRepository.save(User.builder()
                .username("testUser")
                .password("tetsPassword")
                .roles(Arrays.asList(role1, role2))
                .build());
    }
    @AfterEach
    public void finishTest(){
        userRepository.delete(user);
        roleRepository.delete(role1);
        roleRepository.delete(role2);
        privilegeRepository.delete(privilege1);
        privilegeRepository.delete(privilege2);
    }

    @Test
    public void jwTuserRepository_findByUsername_fetchRoles(){
        var user = userRepository.findByUsername("testUser");
        assertEquals(2, user.getRoles().size());
    }
    @Test
    public void jwTuserRepository_findAllUsers_fetchRoles(){
        var users = userRepository.findAllUsers();
        assertEquals(3, users.size());
        assertEquals(2, users.get(2).getRoles().size());
        assertEquals("testUser", users.get(2).getUsername());
    }
    @Test
    public void jwTuserRepository_findByUsername_noRoles_fetchEmptyList(){
        user.setRoles(Arrays.asList());
        var user = userRepository.save(this.user);
        assertEquals(0, user.getRoles().size());
    }
    @Test
    public void jwTuserRepository_findByUsername_nullRolesList_fetchNullValue(){
        user.setRoles(null);
        var user = userRepository.save(this.user);
        assertEquals(null, user.getRoles());
    }
}
