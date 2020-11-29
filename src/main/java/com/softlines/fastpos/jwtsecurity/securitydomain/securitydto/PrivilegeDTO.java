package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PrivilegeDTO {
    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    String name;

    @JsonProperty("RoleIds")
    List<Long> roleIds;

    @JsonProperty("Deleted")
    @Builder.Default
    boolean deleted = false;

}
