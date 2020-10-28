package com.softlines.fastpos.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Additive")
public class Additive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String description;
    String backgroundString;
    int rank;
//     List<Ingredient> Ingrediants ;

}
