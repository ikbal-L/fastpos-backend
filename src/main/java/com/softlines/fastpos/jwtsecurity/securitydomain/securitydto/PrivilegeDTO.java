package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrivilegeDTO {
    long id;
    String name;
}
