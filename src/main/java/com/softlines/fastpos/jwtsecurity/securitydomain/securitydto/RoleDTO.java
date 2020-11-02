package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleDTO {
    long id;
    String name;
    List<Long> privilegeIds;
}
