package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.domain.Descriptor;
import com.softlines.fastpos.validation.customer.CustomerValidationPhoneNumber;
import com.softlines.fastpos.validation.person.PersonValidationPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank(message = MessageKeyConstants.PERSON_NAME_VALIDATION_ERROR)
    String name;

    @JsonProperty("PhoneNumbers")
    Set<String> phoneNumbers;

    @JsonProperty("BackgroundString")
    @NotBlank
    String backgroundString;

    @JsonProperty("IsActive")
    boolean active=true;



}
