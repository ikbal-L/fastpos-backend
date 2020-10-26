package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    long id;
    String name;
    List<Long> privilegeIds;
}
