package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class RoleDTO {
    long id;
    @NotBlank(message = MessageKeyConstants.ROLE_NAME_VALIDATION_ERROR)
    String name;
    List<Long> privilegeIds;
}
