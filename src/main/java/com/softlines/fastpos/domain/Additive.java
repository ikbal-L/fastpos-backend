package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "additive")
public class Additive {

// TODO Add Ingrediants to additive entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "description",nullable = false,unique = true)
    String description ;
    String backgroundString;
    int rank;

//     List<Ingredient> Ingrediants ;

}
