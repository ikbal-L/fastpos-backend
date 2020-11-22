package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class TableDto {

    @JsonProperty("Id")
    long id;
    @JsonProperty("Number")
    int number;
    @JsonProperty("Seats")
    int seats;
    @JsonProperty("IsVirtual")
    boolean virtual;
    @JsonProperty("TableOrders")
    List<Long> tableOrdersId;
}
