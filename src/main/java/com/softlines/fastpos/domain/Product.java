package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    double price;
    String unit;
    boolean isMuchInDemand;
    String type;
    int availableStock;
    String description;
    String backgroundString;
    boolean isPlatter = false;
    int rank;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "products_additives",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "additive_id"))
    List<Additive> additives;
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

}