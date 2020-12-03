package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.validation.customer.CustomerValidationPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank(message = MessageKeyConstants.CUSTOMER_NAME_VALIDATION_ERROR)
    String name;

    @JsonProperty("Mobile")
    @NotBlank(message = MessageKeyConstants.CUSTOMER_MOBILE_VALIDATION_ERROR)
    @CustomerValidationPhoneNumber
    String mobile;


}
