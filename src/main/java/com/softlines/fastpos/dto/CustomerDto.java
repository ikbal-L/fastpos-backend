package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("Name")
    @NotBlank(message = MessageKeyConstants.CUSTOMER_NAME_VALIDATION_ERROR)
    String name;

    @JsonProperty("Mobile")
    @NotBlank(message = MessageKeyConstants.CUSTOMER_MOBILE_VALIDATION_ERROR)
    @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
    String mobile;

    private boolean deleted = false;

}
