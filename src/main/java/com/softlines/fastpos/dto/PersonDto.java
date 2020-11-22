package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Descriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @JsonProperty("Id")
    long id;
    @JsonProperty("Name")
    String name;
    @JsonProperty("PhoneNumber")
    String phoneNumber;
    @JsonProperty("BackgroundString")
    String backgroundString;
    @JsonProperty("IsActive")
    boolean active;
    @JsonProperty("Descriptor")
    Descriptor descriptor;

}
