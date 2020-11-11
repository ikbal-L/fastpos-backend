package com.softlines.fastpos.lazyfetchTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
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
        Role role1 = roleRepository.findRoleById(role.getId()).get();
        assertEquals(2, role1.getPrivileges().size());
        assertEquals("UPDATE_PRIVILEGE", role1.getPrivileges().get(0).getName());
        assertEquals("EDIT_PRIVILEGE", role1.getPrivileges().get(1).getName());
    }

}
