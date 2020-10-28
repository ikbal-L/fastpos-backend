package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {


    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "additives", target = "idAdditives", qualifiedByName = "AdditiveToId")
    @Mapping(source = "category", target = "categoryId", qualifiedByName = "CategoryToId")
    ProductDto toProductDto(Product product);

    List<ProductDto> toProductDTOs(List<Product> products);

    @Mapping(source = "idAdditives", target = "additives", qualifiedByName = "IdToAdditive")
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "IdToCategory")
    Product toProduct(ProductDto productDTO);

    @Named("AdditiveToId")
    public static long AdditiveToId(Additive additives) {
        return additives.getId();
    }

    @Named("CategoryToId")
    public static long CategoryToId(Category category) {
        return category.getId();
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
