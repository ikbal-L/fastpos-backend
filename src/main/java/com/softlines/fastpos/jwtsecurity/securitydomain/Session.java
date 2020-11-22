package com.softlines.fastpos.jwtsecurity.securitydomain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "session")
@Data
@Builder
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private JWTuser user ;

    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;

    private Agent agent ;
    private String ipAddress;
}
