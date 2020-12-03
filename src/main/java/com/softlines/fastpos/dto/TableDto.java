package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class TableDto {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("Number")
    @NotNull
    @Min(value = 1)
    Integer number;

    @JsonProperty("Seats")
    @Min(value = 0)
    int seats;

    @JsonProperty("IsVirtual")
    boolean virtual;


}
