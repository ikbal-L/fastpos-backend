package com.softlines.fastpos.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.validation.ValidationDiscountAmount;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@ValidationDiscountAmount(message = "validation.student.password.match")

public class ProductDto {
    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank
    String name;

    @JsonProperty("Price")
    @Min(value = 1)
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
    @NotBlank
    String backgroundString;

    @JsonProperty("IsPlatter")
    boolean platter;

    @JsonProperty("Rank")
    Integer rank;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("CategoryId")
    Long categoryId;

    @JsonProperty("IdAdditives")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Long> idAdditives;
}
