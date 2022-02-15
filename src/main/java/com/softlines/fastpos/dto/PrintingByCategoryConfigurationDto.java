package com.softlines.fastpos.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.security.securitydomain.securitydto.PrinterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class PrintingByCategoryConfigurationDto {

    Long id;
    String name;
    PrinterDto printer;
    List<Long> categoryIds;
}
