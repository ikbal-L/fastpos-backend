package com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper;

import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(source = "privileges", target = "privilegeIds", qualifiedByName = "privilegesToIds",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    RoleDTO toRoleDto(Role role);

    List<RoleDTO> toRoleDTOs(List<Role> roles);

    @Mapping(source = "privilegeIds", target = "privileges", qualifiedByName = "idsToPrivileges")
    Role toRole(RoleDTO roleDTO);

    @Named("privilegesToIds")
    static long privilegesToIds(Privilege privilege) {
        return privilege.getId();
    }

    @Named("idsToPrivileges")
    static Privilege idsToPrivileges(long idPrivilege) {
        Privilege privilege =new Privilege();
        privilege.setId(idPrivilege);
        return privilege;
    }

    @Named("UsersToIds")
    static JWTuser UsersToIds(long idUser) {
        JWTuser jwTuser =new JWTuser();
        jwTuser.setId(idUser);
        return jwTuser;
    }

    @Named("usersToIds")
    static long usersToIds(JWTuser jwTuser) {
        return jwTuser.getId();
    }

}
