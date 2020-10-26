package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    long id;
    private String username;
    private String password;
    private String pinCode;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private boolean tokenExpired;
    private List<Long> roleIds;
    private long dbInfoId;
}
