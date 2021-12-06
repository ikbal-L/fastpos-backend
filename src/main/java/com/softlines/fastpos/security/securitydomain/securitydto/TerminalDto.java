package com.softlines.fastpos.security.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TerminalDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("LicenceKey")
    String licenceKey;

    @JsonProperty("IsActive")
    boolean active;

    @JsonProperty("AnnexId")
    long annexId;

    @JsonProperty("Deleted")
    private boolean deleted = false;

}
