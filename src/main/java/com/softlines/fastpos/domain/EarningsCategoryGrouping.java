package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class EarningsCategoryGrouping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String category;
    int quantityOfItems;
    double amount;


}
