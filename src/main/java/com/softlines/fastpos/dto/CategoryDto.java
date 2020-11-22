package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;


@Data
public class CategoryDto {

    @JsonProperty("Id")
    long id;
    @JsonProperty("Name")
    String name;
    @JsonProperty("BackgroundString")
    String backgroundString;
    @JsonProperty("Rank")
    Integer rank;
    @JsonProperty("ProductIds")
    List<Long> productIds;

}
