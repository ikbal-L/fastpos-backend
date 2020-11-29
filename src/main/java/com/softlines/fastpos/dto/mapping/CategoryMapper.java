package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.CategoryDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "products", target = "productIds", qualifiedByName = "ProductToId"
            ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    CategoryDto toCategoryDto(Category category);


    List<CategoryDto> toCategoryDTOs(List<Category> category);

    @Mapping(source = "productIds", target = "products", qualifiedByName = "ProductToId"
            ,nullValueCheckStrategy =NullValueCheckStrategy.ALWAYS)
    Category toCategory(CategoryDto categoryDTO);

    List<Category> toCategories(List<CategoryDto> categoryDtos);

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
