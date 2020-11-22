package com.softlines.fastpos.jwtsecurity.securitydomain;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurent_id")
    List<Annex> annexes;

}
