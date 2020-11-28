package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;

import com.softlines.fastpos.jwtsecurity.securitydomain.Agent;
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
    @JsonProperty("Username")
    private String username;

    @NotBlank(message = MessageKeyConstants.USER_PASSWORD_VALIDATION_ERROR)
    @JsonProperty("Password")
    private String password;

    private String pinCode;
    private String firstName;

    private String lastName;

    private String email;

    @NotNull
    private Boolean enabled;

    private boolean tokenExpired;

    private List<Long> roleIds;

    private List<Long> annexesIds;

    @JsonProperty("TerminalId")
    private long terminalId;

    @JsonProperty("Agent")
    private Agent agent ;
}
