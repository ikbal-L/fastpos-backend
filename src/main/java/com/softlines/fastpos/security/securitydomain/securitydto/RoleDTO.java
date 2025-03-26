package com.softlines.fastpos.security.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class RoleDTO {

    long id;
    @NotBlank(message = MessageKeyConstants.ROLE_NAME_VALIDATION_ERROR)
    String name;
    List<Long> privilegeIds;
    @JsonProperty("IsPredefined")
    boolean predefined;


}
