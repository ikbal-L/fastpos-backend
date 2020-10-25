package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "products", target = "idProducts", qualifiedByName = "ProductToId")
    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDTOs(List<Category> category);

    Category toCategory(CategoryDto categoryDTO);


    @Named("ProductToId")
    public static long ProductToId(Product products) {
        return products.getId();
    }
}
