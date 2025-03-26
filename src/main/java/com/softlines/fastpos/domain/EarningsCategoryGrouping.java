package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
