package com.softlines.fastpos.jwtsecurity.securitydomain;


import com.softlines.fastpos.domain.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE JWTuser SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@SuperBuilder
public class JWTuser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "token_expired")
    private boolean tokenExpired;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @ManyToMany(mappedBy = "users",fetch =  FetchType.EAGER)
    private List<Annex> annexes;

    @Enumerated(EnumType.STRING)
    private Agent agent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_phone_numbers",joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_phone_number")
    private Set<String> phoneNumbers;


    @Builder.Default
    @NotNull
    private boolean deleted = false;


}
