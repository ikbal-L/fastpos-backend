package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class CategoryDto {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("Name")
    @NotBlank(message =MessageKeyConstants.CATEGORY_NAME_VALIDATION_ERROR)
    String name;

    @JsonProperty("BackgroundString")
    @NotBlank(message = MessageKeyConstants.CATEGORY_BACKGROUND_STRING_VALIDATION_ERROR)
    String backgroundString;

    @JsonProperty("Rank")
    @Min(1)
    Integer rank;

    @JsonProperty("ProductIds")
    List<Long> productIds;

    private boolean deleted = false;

}
