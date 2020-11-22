package com.softlines.fastpos.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    boolean isMuchInDemand;
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
    Integer rank;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("CategoryId")
    Long categoryId;
    @JsonProperty("IdAdditives")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Long> idAdditives;
}
