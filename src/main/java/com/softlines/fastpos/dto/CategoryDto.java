package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class CategoryDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank
    String name;

    @JsonProperty("BackgroundString")
    @NotBlank
    String backgroundString;

    @JsonProperty("Rank")
    Integer rank;

    @JsonProperty("ProductIds")
    List<Long> productIds;

}
