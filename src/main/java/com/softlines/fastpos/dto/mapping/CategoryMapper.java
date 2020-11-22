package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.CategoryDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Named("Generic")
    CategoryDto toCategoryDto(Category category);

    @Named("WithProducts")
    @Mapping(source = "products", target = "productIds", qualifiedByName = "ProductToId")
    CategoryDto toCategoryDtoWithProducts(Category category);

    @IterableMapping(qualifiedByName = "Generic")
    List<CategoryDto> toCategoryDTOs(List<Category> category);

    @IterableMapping(qualifiedByName = "WithProducts")
    List<CategoryDto> toCategoryDTOsWithProducts(List<Category> category);

    @Mapping(source = "productIds", target = "products", qualifiedByName = "ProductToId")
    Category toCategory(CategoryDto categoryDTO);

    @Named("ProductToId")
    public static long ProductToId(Product products) {
        return products.getId();
    }

    @Named("IdToProduct")
    public static Product ProductToId(long productId) {
        Product product = new Product();
        product.setId(productId);
        return product;
    }

}
