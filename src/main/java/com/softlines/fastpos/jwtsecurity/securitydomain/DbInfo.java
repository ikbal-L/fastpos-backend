package com.softlines.fastpos.jwtsecurity.securitydomain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dbinfo")
@Data
public class DbInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "driver_class_name")
    private String driverClassName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "dbId")
    private List<JWTuser> jwTusers;

}
