package com.softlines.fastpos.dto;

import lombok.Data;

@Data
public class TerminalDto {

    long id;
    String licenceKey;
    boolean isActive;
    Long annexId;


}
