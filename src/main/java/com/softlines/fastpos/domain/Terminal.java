package com.softlines.fastpos.domain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Terminal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String licenceKey;

    @Column(name = "isActive")
    boolean active;
    // TODO check relation between annex and terminal
    @ManyToOne
    Annex annex;


}
