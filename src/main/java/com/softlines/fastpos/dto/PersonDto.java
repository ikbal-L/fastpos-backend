package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.domain.Descriptor;
import com.softlines.fastpos.validation.customer.CustomerValidationPhoneNumber;
import com.softlines.fastpos.validation.person.PersonValidationPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank(message = MessageKeyConstants.PERSON_NAME_VALIDATION_ERROR)
    String name;

    @JsonProperty("PhoneNumber")
    @PersonValidationPhoneNumber
    String phoneNumber;

    @JsonProperty("BackgroundString")
    String backgroundString;

    @JsonProperty("IsActive")
    boolean active=true;



}
