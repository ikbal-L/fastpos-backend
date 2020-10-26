package com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper;

import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "roles", target = "roleIds", qualifiedByName = "rolessToIds")
    @Mapping(source = "dbInfo", target = "dbInfoId", qualifiedByName = "dbInfoToId")
    UserDTO toUserDto(JWTuser jwTuser);

    List<UserDTO> toUserDTOs(List<JWTuser> jwTusers);

    JWTuser toJWTuser(UserDTO userDTO);

    @Named("rolessToIds")
    static long rolessToIds(Role role) {
        return role.getId();
    }
    @Named("dbInfoToId")
    static Long dbInfoToId(DbInfo dbInfo){
        if(dbInfo != null) return dbInfo.getId();
        return 0l;
    }
}
