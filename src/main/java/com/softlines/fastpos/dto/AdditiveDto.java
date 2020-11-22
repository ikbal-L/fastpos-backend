package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

// TODO Remove additiveDto


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditiveDto {

    @JsonProperty("Id")
    long id;
    @JsonProperty("Description")
    @NotBlank(message = "validation.error.additive.description")
    String description;
    @JsonProperty("BackgroundString")
    @NotBlank(message = "validation.error.additive.background")
    String backgroundString;
    @JsonProperty("Rank")
    int rank;

}
