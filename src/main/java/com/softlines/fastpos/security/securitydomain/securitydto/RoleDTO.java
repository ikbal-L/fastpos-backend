package com.softlines.fastpos.security.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class RoleDTO {
    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    @NotBlank(message = MessageKeyConstants.ROLE_NAME_VALIDATION_ERROR)
    String name;

    @JsonProperty("PrivilegeIds")
    List<Long> privilegeIds;




}
