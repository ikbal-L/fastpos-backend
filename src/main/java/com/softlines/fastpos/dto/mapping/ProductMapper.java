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

//    @Named("Generic")
//    ProductDto toProductDto(Product product);

    @Named("Generic")
    @Mapping(source = "additives", target = "idAdditives", qualifiedByName = "AdditiveToId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "category", target = "categoryId", qualifiedByName = "CategoryToId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS )
    ProductDto toProductDto(Product product);


//    @IterableMapping(qualifiedByName = "Generic")
    List<ProductDto> toProductDTOs(List<Product> products);


    @Mapping(source = "idAdditives", target = "additives", qualifiedByName = "IdToAdditive")
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "IdToCategory")
    Product toProduct(ProductDto productDTO);

    Product toProductWithCategory(ProductDto productDTO);

    @Named("AdditiveToId")
    public static long AdditiveToId(Additive additives) {
        return additives.getId();
    }

    @Named("CategoryToId")
    public static long CategoryToId(Category category) {
        if (category!=null){
            return category.getId();
        }
        return  0;
    }


    @Named("IdToAdditive")
    public static Additive IdToAdditive(long idAdditive) {
        Additive additive = new Additive();
        additive.setId(idAdditive);
        return additive;
    }



    @Named("IdToCategory")
    public static Category IdToCategory(long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }


}
