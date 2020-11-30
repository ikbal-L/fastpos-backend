package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;

import com.softlines.fastpos.jwtsecurity.securitydomain.Agent;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("Id")
    long id;

    @NotBlank(message = MessageKeyConstants.USER_USERNAME_VALIDATION_ERROR)
    @JsonProperty("Username")
    private String username;

    @NotBlank(message = MessageKeyConstants.USER_PASSWORD_VALIDATION_ERROR)
    @JsonProperty("Password")
    private String password;

    @JsonProperty("PinCode")
    private String pinCode;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("Email")
    @Email
    private String email;

    @NotNull
    @JsonProperty("Enabled")
    private Boolean enabled;

    @JsonProperty("TokenExpired")
    private boolean tokenExpired;

    @JsonProperty("RoleIds")
    private List<Long> roleIds;

    @JsonProperty("AnnexesIds")
    private List<Long> annexesIds;

    @JsonProperty("TerminalId")
    private long terminalId;

    @JsonProperty("Agent")
    private Agent agent;

    @JsonProperty("Deleted")
    @Builder.Default
    boolean deleted = false;

}
