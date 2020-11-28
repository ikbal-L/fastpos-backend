package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdditiveMapper {

    AdditiveMapper INSTANCE = Mappers.getMapper(AdditiveMapper.class);

//    @Mapping(source = "products", target = "productsId", qualifiedByName = "ProductToId")
    AdditiveDto toAdditiveDto(Additive additive);
    List<AdditiveDto> toAdditiveDTOs(List<Additive> additives);

    Additive toAdditive(AdditiveDto additiveDto);
    List<Additive> toAdditiveList(List<AdditiveDto> additiveDtoList);

    @Named("ProductToId")
    public static long ProductsToIds(Product product) {
        return product.getId() ;
    }
}
