package com.softlines.fastpos.lazyfetchTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
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
    JWTuserRepository jwTuserRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    JWTuser jwTuser;
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
        jwTuser = jwTuserRepository.save(JWTuser.builder()
                .username("testUser")
                .password("tetsPassword")
                .roles(Arrays.asList(role1, role2))
                .build());
    }
    @AfterEach
    public void finishTest(){
        jwTuserRepository.delete(jwTuser);
        roleRepository.delete(role1);
        roleRepository.delete(role2);
        privilegeRepository.delete(privilege1);
        privilegeRepository.delete(privilege2);
    }

    @Test
    public void jwTuserRepository_findByUsername_fetchRoles(){
        var user = jwTuserRepository.findByUsername("testUser");
        assertEquals(2, user.getRoles().size());
    }
    @Test
    public void jwTuserRepository_findAllUsers_fetchRoles(){
        var users = jwTuserRepository.findAllUsers();
        assertEquals(3, users.size());
        assertEquals(2, users.get(2).getRoles().size());
        assertEquals("testUser", users.get(2).getUsername());
    }
    @Test
    public void jwTuserRepository_findByUsername_noRoles_fetchEmptyList(){
        jwTuser.setRoles(Arrays.asList());
        var user = jwTuserRepository.save(jwTuser);
        assertEquals(0, user.getRoles().size());
    }
    @Test
    public void jwTuserRepository_findByUsername_nullRolesList_fetchNullValue(){
        jwTuser.setRoles(null);
        var user = jwTuserRepository.save(jwTuser);
        assertEquals(null, user.getRoles());
    }
}
