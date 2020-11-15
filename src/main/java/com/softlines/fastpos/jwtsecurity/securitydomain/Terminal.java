package com.softlines.fastpos.jwtsecurity.securitydomain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Terminal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String licenceKey;
    boolean isActive;
    // TODO check relation between annex and terminal
    @ManyToOne
    Annex annex;


}
