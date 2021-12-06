package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Person;
import com.softlines.fastpos.dto.PersonDto;
import com.softlines.fastpos.security.securitydomain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

//    @Mapping(source = "roles", target = "rolesId", qualifiedByName = "IdToRole")
    PersonDto toPersonDto(Person person);

    List<PersonDto> toPersonDTOs(List<Person> persons);

//    @Mapping(source = "rolesId", target = "roles", qualifiedByName = "RoleToId")
    Person toPerson(PersonDto personDTO);

    @Named("IdToRole")
    public static long IdToRole(Role role ) {
        return role.getId();
    }

    @Named("RoleToId")
    public static Role RoleToId(long productId) {
        Role role = new Role();
        role.setId(productId);
        return role;
    }

}
