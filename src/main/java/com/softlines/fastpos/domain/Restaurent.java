package com.softlines.fastpos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurent_id")
    List<Annex> annexes;

}
