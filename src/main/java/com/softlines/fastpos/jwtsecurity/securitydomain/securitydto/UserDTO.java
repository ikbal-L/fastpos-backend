package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class UserDTO {
    long id;
    @NotBlank(message = "validation.user.error.username")
    private String username;
    @NotBlank(message = "validation.user.error.password")
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
}
