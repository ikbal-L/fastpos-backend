package com.softlines.fastpos.dto;

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
    //    User owner;
    @JsonProperty("AnnexesId")
    List<Long> annexesId;

}
