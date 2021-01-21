package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "additives", target = "idAdditives", qualifiedByName = "AdditiveToId"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "category.id", target = "categoryId"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    ProductDto toProductDto(Product product);


    List<ProductDto> toProductDTOs(List<Product> products);


    @Mapping(source = "idAdditives", target = "additives", qualifiedByName = "IdToAdditive"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "categoryId", target = "category"
            , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,qualifiedByName = "categoryIdToCategory")
    Product toProduct(ProductDto productDTO);

    List<Product> toProductList(List<ProductDto> productDtoList);

    @Named("AdditiveToId")
    public static long AdditiveToId(Additive additives) {
        return additives.getId();
    }


    @Named("IdToAdditive")
    public static Additive IdToAdditive(Long idAdditive) {
        if (idAdditive==null)return null;
        Additive additive = new Additive();
        additive.setId(idAdditive);
        return additive;
    }
    @Named("categoryIdToCategory")
    public static Category categoryIdToCategory(Long categoryId){
        if (categoryId == null) return null;
        return Category.builder().id(categoryId).build();
    }


}
