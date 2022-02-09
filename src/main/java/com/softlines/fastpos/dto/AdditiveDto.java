package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.domain.AdditiveSate;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


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

    @JsonProperty("IsFavorite")
    boolean favorite;

//    @JsonProperty("TimeStamp")
//    Date timestamp;

//    @NotNull
//    @JsonProperty("State")
//    AdditiveSate sate;

    @JsonProperty("ImageUrl")
    String imageUrl;

}
