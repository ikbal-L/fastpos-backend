package com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper;

import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.PrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {

    PrivilegeMapper INSTANCE = Mappers.getMapper(PrivilegeMapper.class);

    PrivilegeDTO toPrivilegeDTO(Privilege privilege);

    List<PrivilegeDTO> toPrivilegeDTOs(List<Privilege> privileges);

    Privilege toPrivilege(PrivilegeDTO privilegeDTO);
}
