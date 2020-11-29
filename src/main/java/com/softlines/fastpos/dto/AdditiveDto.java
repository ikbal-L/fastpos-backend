package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditiveDto {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("Description")
    @NotBlank(message = MessageKeyConstants.ADDITIVE_DESCRIPTION_VALIDATION_ERROR)
    String description;

    @JsonProperty("BackgroundString")
    @NotBlank(message = MessageKeyConstants.ADDITIVE_BACKGROUND_STRING_VALIDATION_ERROR)
    String backgroundString;

    @JsonProperty("Rank")
    @NotNull
    @Min(1)
    Integer rank;

    @Builder.Default
    boolean deleted = false;

}
