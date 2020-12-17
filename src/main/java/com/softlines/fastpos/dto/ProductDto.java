package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.softlines.fastpos.validation.product.ProductValidationIfCategoryIdEqualNullRankMustEqualNull;
import lombok.*;

import com.softlines.fastpos.validation.order.OrderDtoValidationDiscountAmountGreaterThanTotal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;

@Data
@ProductValidationIfCategoryIdEqualNullRankMustEqualNull
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
    @Min(1)
    @NotNull
    Integer rank;

    @JsonProperty("CategoryId")
    Long categoryId;

    @JsonProperty("IdAdditives")
    List<Long> idAdditives;


}
