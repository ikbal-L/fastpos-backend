package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.Descriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank
    String name;

    @JsonProperty("PhoneNumber")
    // TODO pattern
    String phoneNumber;

    @JsonProperty("BackgroundString")
    String backgroundString;

    @JsonProperty("IsActive")
    boolean active=true;


}
