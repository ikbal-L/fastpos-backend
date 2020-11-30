package com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper;

import com.softlines.fastpos.jwtsecurity.securitydomain.Annex;
import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "roles", target = "roleIds", qualifiedByName = "rolessToIds",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "annexes",target = "annexesIds",qualifiedByName = "annexesToIds",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    UserDTO toUserDto(JWTuser jwTuser);


    List<UserDTO> toUserDTOs(List<JWTuser> jwTusers);



    @Mapping(source = "roleIds", target = "roles", qualifiedByName = "idsToRoles")
    JWTuser toJWTuser(UserDTO userDTO);

    @Named("rolesToIds")
    static long rolesToIds(Role role) {
        return role.getId();
    }
    @Named("idsToRoles")
    static Role idsToRoles(long roleId) {
        Role role = new Role();
        role.setId(roleId);
        return role;
    }

    @Named("annexesToIds")
    static long annexesToIds(Annex annex) {
        return annex.getId();
    }
    @Named("idsToAnnexes")
    static Annex idsToAnnexes(long roleId) {
        Annex annex = new Annex();
        annex.setId(roleId);
        return annex;
    }
}
