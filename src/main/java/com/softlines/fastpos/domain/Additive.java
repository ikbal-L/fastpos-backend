package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


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
    @NotBlank(message = "validation.additive.error.description")
    String description ;
    @NotBlank(message = "validation.additive.error.background")
    String backgroundString;
    int rank;
}
