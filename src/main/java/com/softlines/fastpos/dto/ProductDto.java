package com.softlines.fastpos.dto;

//import lombok.Data;
import lombok.*;

import java.util.List;


@Data
public class ProductDto {
    long id;
    String name;
    double price;
    String unit;
    boolean isMuchInDemand;
    String type;
    int availableStock;
    String description;
    String backgroundString;
    boolean isPlatter ;
    int rank;
    long categoryId;
    List<Long> idAdditives;
}
