package com.softlines.fastpos.security.securitydomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Restaurent SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
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

    @Builder.Default
    @NotNull
    private boolean deleted = false;
}
