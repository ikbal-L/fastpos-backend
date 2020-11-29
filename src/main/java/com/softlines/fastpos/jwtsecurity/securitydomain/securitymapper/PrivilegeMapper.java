package com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.PrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {

    PrivilegeMapper INSTANCE = Mappers.getMapper(PrivilegeMapper.class);

    @Mapping(source = "roles", target = "roleIds", qualifiedByName = "rolesToIds"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    PrivilegeDTO toPrivilegeDTO(Privilege privilege);

    List<PrivilegeDTO> toPrivilegeDTOs(List<Privilege> privileges);

    @Mapping(source = "roleIds", target = "roles", qualifiedByName = "idsToRoles"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Privilege toPrivilege(PrivilegeDTO privilegeDTO);

    @Named("idsToRoles")
    static Role idsToPrivileges(long roleId) {
        Role role = new Role();
        role.setId(roleId);
        return role;
    }

    @Named("rolesToIds")
    static long rolesToIds(Role role) {
        return role.getId();
    }

}
