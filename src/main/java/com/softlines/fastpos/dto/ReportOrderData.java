package com.softlines.fastpos.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ReportOrderData {

    long id;
    long orderNumber;
    LocalDateTime orderTime;
    double total;
    double newTotal;
    String canceledBy;
}
