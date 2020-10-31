package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.domain.Restaurent;
import com.softlines.fastpos.dto.RestaurentDto;
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

    Restaurent toRestaurent(RestaurentDto RestaurentDTO);

    @Named("annexToId")
    public static long annexToId(Annex annex) {
        return annex.getId();
    }

}
