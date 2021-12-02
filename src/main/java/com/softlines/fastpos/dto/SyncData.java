package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SyncData {

    @JsonProperty("Type")
    protected String type;

    @JsonProperty("Id")
    protected Long id;
    @JsonProperty("IsLocked")
    boolean isLocked;

    @JsonProperty("LockedBy")
    protected String lockedBy;
}
