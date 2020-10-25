package com.softlines.fastpos.dto;


import lombok.*;

import java.util.List;


@Data
public class AdditiveDto {
    long id;
    String description;
    String backgroundString;
    int rank;
    List<Long> productsId;

}
