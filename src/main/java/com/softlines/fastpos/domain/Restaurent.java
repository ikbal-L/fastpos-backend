package com.softlines.fastpos.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Restaurent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false)

    String name;
    String address;
    String serverLicenceKey;
    //  TODO add User owner to restaurent
    //    User owner;

    //  TODO Relation between Annex and Restaurent
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurent_id")
    List<Annex> annexes;

}
