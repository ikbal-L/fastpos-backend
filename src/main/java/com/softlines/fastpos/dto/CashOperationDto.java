package com.softlines.fastpos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashOperationDto {
    long id;
    Date date;
    float amount;
    Long paymentId;
    Long orderId;
}
