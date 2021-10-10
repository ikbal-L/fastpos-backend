package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class OrderReportInfo {

    @Id
    long id;
    long orderNumber;
    double total;
    Date date;

    @JsonIgnore
    @ManyToOne
    DailyExpenseReport report;
}
