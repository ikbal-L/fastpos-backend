package com.softlines.fastpos.jwtsecurity.securitydomain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

//    @OneToMany//(mappedBy = "annex")
//    List<Terminal> terminals;

}
