package com.softlines.fastpos.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.util.List;


@Data
public class ProductDto {
    @JsonProperty("Id")
    long id;
    @JsonProperty("Name")
    String name;
    @JsonProperty("Price")
    double price;
    @JsonProperty("Unit")
    String unit;
    @JsonProperty("IsMuchInDemand")
    boolean muchInDemand;
    @JsonProperty("Type")
    String type;
    @JsonProperty("AvailableStock")
    int availableStock;

    @JsonProperty("Description")
    String description;

    @JsonProperty("BackgroundString")
    String backgroundString;

    @JsonProperty("IsPlatter")
    boolean platter;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("Rank")
    @JsonSetter()
    Integer rank;

    //    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("CategoryId")
    Long categoryId;

    @JsonProperty("IdAdditives")
    List<Long> idAdditives;
}
