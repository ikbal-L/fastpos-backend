package com.softlines.fastpos.mappingUnitTesting;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.jwtsecurity.jwtcontroller.RoleController;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
public class RoleMappingUnitTesting {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    RoleController roleController;
    //DTO to Role
    @Test
    public void roleMapper_roleDTOToRole_PrivilegesNotEmpty() {
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
    public void roleMapper_roleDTOToRole_PrivilegesEmpty() {
        var roleDTO = RoleDTO.builder()
                .id(1l)
                .name("ROLE_HR")
                .privilegeIds(Arrays.asList()).build();
        var role = roleMapper.toRole(roleDTO);
        assertEquals(role.getPrivileges().size(), 0);
    }
    @Test
    public void roleMapper_roleDTOToRole_PrivilegesNull() {
        var roleDTO = RoleDTO.builder()
                .id(1l)
                .name("ROLE_HR")
                .privilegeIds(null).build();
        var role = roleMapper.toRole(roleDTO);
        assertEquals(role.getPrivileges(), null);
    }
   @Test
    public void roleMapper_roleDTOToRole_RoleDTOIsNull() {
        var role = roleMapper.toRole(null);
        assertEquals(role, null);
    }
    //Role to DTO
    @Test
    public void roleMapper_roleToRoleDTO_PrivilegesNotEmpty() {
        var privilege1 = Privilege.builder()
                .id(1l)
                .name("READ_PRIVILEGE").build();
        var privilege2 = Privilege.builder()
                .id(2l)
                .name("WRITE_PRIVILEGE").build();
        var role = Role.builder()
                .id(1l)
                .name("ROLE_ADMIN")
                .privileges(Arrays.asList(privilege1, privilege2)).build();
        var roleDTO = roleMapper.toRoleDto(role);
        assertEquals(roleDTO.getId(), role.getId());
        assertEquals(roleDTO.getName(), role.getName());
        assertEquals(roleDTO.getPrivilegeIds().size(), role.getPrivileges().size());
        assertEquals(2, role.getPrivileges().size());
        assertEquals(roleDTO.getPrivilegeIds().get(0), role.getPrivileges().get(0).getId());
    }
    @Test
    public void roleMapper_roleToRoleDTO_PrivilegesEmpty() {
        var role = Role.builder()
                .id(1l)
                .name("ROLE_HR")
                .privileges(Arrays.asList()).build();
        var roleDTO = roleMapper.toRoleDto(role);
        assertEquals(0, roleDTO.getPrivilegeIds().size());
    }
    @Test
    public void roleMapper_roleToRoleDTO_PrivilegesNull() {
        var role = Role.builder()
                .id(1l)
                .name("ROLE_HR")
                .privileges(null).build();
        var roleDTO = roleMapper.toRoleDto(role);
        assertEquals(null, roleDTO.getPrivilegeIds());
    }
   @Test
    public void roleMapper_roleToRoleDTO_RoleIsNull() {
        var roleDTO = roleMapper.toRoleDto(null);
        assertEquals(roleDTO, null);
    }

}
