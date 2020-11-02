package com.softlines.fastpos.mappingUnitTesting;

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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class RoleMappingUnitTesting {
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    RoleController roleController;

    @Test
    public void roleMapperTurnsRoleToRoleDTO_PrivilegesNotEmpty() {
        var roleDTO = RoleDTO.builder()
                .id(1l)
                .name("ROLE_HR")
                .privilegeIds(Arrays.asList(1l)).build();
        var role = roleMapper.toRole(roleDTO);
        assertEquals(role.getId(), roleDTO.getId());
        assertEquals(role.getName(), roleDTO.getName());
        assertEquals(role.getPrivileges().size(), roleDTO.getPrivilegeIds().size());
        assertEquals(role.getPrivileges().size(), 1);
        assertEquals(role.getPrivileges().get(0).getId(), roleDTO.getPrivilegeIds().get(0));
    }

    @Test
    public void roleMapperTurnsRoleToRoleDTO_PrivilegesEmpty() {
        var roleDTO = RoleDTO.builder()
                .id(1l)
                .name("ROLE_HR")
                .privilegeIds(Arrays.asList()).build();
        var role = roleMapper.toRole(roleDTO);
        assertEquals(role.getPrivileges().size(), 0);
    }

    @Test
    public void roleMapperTurnsRoleToRoleDTO_PrivilegesNull() {
        var roleDTO = RoleDTO.builder()
                .id(1l)
                .name("ROLE_HR")
                .privilegeIds(null).build();
        var role = roleMapper.toRole(roleDTO);
        assertEquals(role.getPrivileges(), null);
    }

   @Test
    public void roleMapperTurnsRoleToRoleDTO_RoleDTOIsNull() {
        var role = roleMapper.toRole(null);
        assertEquals(role, null);
    }

}
