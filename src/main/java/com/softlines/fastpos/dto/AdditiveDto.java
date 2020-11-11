package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.util.List;
// TODO Remove additiveDto


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditiveDto {

    @JsonProperty("Id")
    long id;
    @JsonProperty("Description")
    String description;
    @JsonProperty("BackgroundString")
    String backgroundString;
    @JsonProperty("Rank")
    int rank;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AdditiveDtoBuilder {

    }
}
