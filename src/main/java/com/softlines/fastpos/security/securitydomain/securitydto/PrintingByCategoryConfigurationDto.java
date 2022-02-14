package com.softlines.fastpos.security.securitydomain.securitydto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class PrintingByCategoryConfigurationDto {

    Long id;
    String name;
    PrinterDto printer;
    List<Long> categories;
}
