package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TerminalDto {

    long id;
    String licenceKey;
    @JsonProperty("IsActive")
    boolean active;
    long annexId;


}
