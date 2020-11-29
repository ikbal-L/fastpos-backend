package com.softlines.fastpos.jwtsecurity.securitydomain.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class RestaurentDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("Name")
    String name;

    @JsonProperty("Address")
    String address;

    @JsonProperty("ServerLicenceKey")
    String serverLicenceKey;

    @JsonProperty("AnnexesId")
    List<Long> annexesId;

    @JsonProperty("Deleted")
    private boolean deleted = false;

}
