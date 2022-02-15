package com.softlines.fastpos.security.securitydomain.securitymapper;


import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.PrintingByCategoryConfiguration;
import com.softlines.fastpos.dto.PrintingByCategoryConfigurationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrintingByCategoryConfigurationMapper {

    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "CategoryToId"
            ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    PrintingByCategoryConfigurationDto toDto(PrintingByCategoryConfiguration entity);

    List<PrintingByCategoryConfigurationDto> toDTOs(List<PrintingByCategoryConfiguration> printingByCategoryConfigurations);


    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "CategoryToId"
            ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void toExistingDto(PrintingByCategoryConfiguration entity, @MappingTarget PrintingByCategoryConfigurationDto dto);


    @Mapping(source = "categoryIds", target = "categories", qualifiedByName = "IdToCategory"
            ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    PrintingByCategoryConfiguration toEntity(PrintingByCategoryConfigurationDto entity);

    public static  Long CategoryToId(Category category){
        return category.getId();
    }

    public static  Category IdToCategory(Long id){
        return  Category.builder().id(id).build();
    }
}
