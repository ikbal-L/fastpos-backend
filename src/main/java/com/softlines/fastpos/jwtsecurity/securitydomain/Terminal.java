package com.softlines.fastpos.jwtsecurity.securitydomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
