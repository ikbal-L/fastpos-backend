package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.validation.PhoneNumberCollection;
import com.softlines.fastpos.validation.customer.CustomerValidationPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class CustomerDto {

    long id;

    String name;

    @PhoneNumberCollection
    Set<String> phoneNumbers;

    double balance;

    String address;

}
