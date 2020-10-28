package com.softlines.fastpos.dto;


import com.softlines.fastpos.domain.Order;

import lombok.Data;
import java.util.List;


@Data
public class TableDto {
    long id;
    int number;
    int seats;
    boolean isVirtual;
    List<Long> tableOrdersId;
}
