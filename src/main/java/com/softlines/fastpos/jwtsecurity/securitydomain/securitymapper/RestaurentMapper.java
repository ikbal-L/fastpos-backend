package com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper;

import com.softlines.fastpos.jwtsecurity.securitydomain.Annex;
import com.softlines.fastpos.jwtsecurity.securitydomain.Restaurent;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RestaurentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurentMapper {

    RestaurentMapper INSTANCE = Mappers.getMapper(RestaurentMapper.class);

    @Mapping(source = "annexes", target = "annexesId", qualifiedByName = "annexToId")
    RestaurentDto toRestaurentDto(Restaurent Restaurent);

    List<RestaurentDto> toRestaurentDTOs(List<Restaurent> Restaurent);

    @Mapping(source = "annexesId", target = "annexes", qualifiedByName = "IdToAnnex")
    Restaurent toRestaurent(RestaurentDto RestaurentDTO);

    @Named("annexToId")
    public static long annexToId(Annex annex) {
        return annex.getId();
    }

    @Named("IdToAnnex")
    public static Annex IdToAnnex(long id) {
        Annex annex = new Annex();
        annex.setId(id);
        return annex;
    }

}
