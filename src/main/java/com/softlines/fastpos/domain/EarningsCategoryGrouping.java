package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class EarningsCategoryGrouping {

    String category;
    int quantityOfItems;
    double amount;
    @Id
    private String id;

}
