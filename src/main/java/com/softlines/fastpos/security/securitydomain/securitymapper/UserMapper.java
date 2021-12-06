package com.softlines.fastpos.security.securitydomain.securitymapper;

import com.softlines.fastpos.security.securitydomain.Annex;
import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Role;
import com.softlines.fastpos.security.securitydomain.securitydto.UserDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    List<UserDTO> toUserDTOs(List<User> users);

    @Mapping(source = "roles", target = "roleIds", qualifiedByName = "rolesToIds"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "annexes", target = "annexesIds", qualifiedByName = "annexesToIds"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "terminalId", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDto(User user);



    @Mapping(source = "roleIds", target = "roles", qualifiedByName = "idsToRoles"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "annexesIds", target = "annexes", qualifiedByName = "idsToAnnexes"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    User toJWTuser(UserDTO userDTO);

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
