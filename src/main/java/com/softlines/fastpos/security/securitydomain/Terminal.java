package com.softlines.fastpos.security.securitydomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Terminal SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
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


    @Builder.Default
    @NotNull
    private boolean deleted = false;
}
