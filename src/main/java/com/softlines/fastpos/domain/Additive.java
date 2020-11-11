package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("Id")
    long id;
    @Column(name = "description",nullable = false,unique = true)
    @JsonProperty("Description")
    String description ;
    @JsonProperty("BackgroundString")
    String backgroundString;
    @JsonProperty("Rank")
    int rank;

//     List<Ingredient> Ingrediants ;

}
