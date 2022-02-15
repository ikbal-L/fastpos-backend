package com.softlines.fastpos.security.securitydomain.securitymapper;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Printer;
import com.softlines.fastpos.domain.PrintingByCategoryConfiguration;
import com.softlines.fastpos.dto.PrintingByCategoryConfigurationDto;
import com.softlines.fastpos.security.securitydomain.securitydto.PrinterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrinterMapper {


    PrinterDto toDto(Printer entity);

    List<PrinterDto> toDTOs(List<Printer> printingByCategoryConfigurations);



    void toExistingDto(Printer entity, @MappingTarget PrinterDto dto);
    void toExistingDtos(List<Printer> entities, @MappingTarget List<PrinterDto> dtoList);



    Printer toEntity(PrinterDto dto);
    List<Printer>toEntities(List<PrinterDto> dtoList);

//    public static  Long CategoryToId(Printer category){
//        return category.getId();
//    }
//
//    public static  Printer IdToCategory(Long id){
//
//        return  Printer.builder().id(id).build();
//    }
}
