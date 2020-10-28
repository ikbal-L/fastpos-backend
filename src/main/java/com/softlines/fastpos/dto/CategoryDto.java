package com.softlines.fastpos.dto;

import lombok.*;
import java.util.List;


@Data
public class CategoryDto {

    long id;
    String name;
    String backgroundString;
    int rank;
    List<Long> idProducts;

}
