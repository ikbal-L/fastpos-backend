package com.softlines.fastpos.security.securitydomain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dbinfo")
@SQLDelete(sql = "UPDATE DbInfo SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
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

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dbId")
//    private List<JWTuser> jwTusers;

    @NotNull
    private boolean deleted = false;
}
