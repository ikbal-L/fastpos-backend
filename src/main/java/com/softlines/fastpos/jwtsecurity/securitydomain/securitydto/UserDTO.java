package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import com.softlines.fastpos.constants.MessageKeyConstants;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    long id;
    @NotBlank(message = MessageKeyConstants.USER_USERNAME_VALIDATION_ERROR)
    private String username;
    @NotBlank(message = MessageKeyConstants.USER_PASSWORD_VALIDATION_ERROR)
    private String password;
    private String pinCode;
    private String firstName;
    private String lastName;
    private String email;
    @NotNull
    private Boolean enabled;
    private boolean tokenExpired;
    private List<Long> roleIds;
    private long dbId;

    private long annexId;
    private long terminalId;
}
