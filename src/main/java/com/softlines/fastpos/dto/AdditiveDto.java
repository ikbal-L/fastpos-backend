package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


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
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    Integer rank;
}
