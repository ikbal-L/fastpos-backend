package com.softlines.fastpos.security.securitydomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Annex SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Annex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonProperty("Id")
    long id;
//    @JsonProperty("Name")
    String name;
//    @JsonProperty("Address")
    String address;
//    @JsonProperty("ServerLicenceKey")
    String serverLicenceKey;
    @OneToOne
    private DbInfo dbInfo;
//    @OneToMany(mappedBy = "annex")
//    List<Terminal> terminals;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "annex_user",
            joinColumns = @JoinColumn(name = "annex_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> users;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

    @OneToOne
    AnnexConfiguration configuration;
}
