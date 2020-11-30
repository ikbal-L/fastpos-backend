package com.softlines.fastpos.jwtsecurity.securitydomain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE Privilege SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = MessageKeyConstants.PRIVILEGE_NAME_VALIDATION_ERROR)
    @Column(name = "name", unique = true)
    String name;

    @ManyToMany(mappedBy = "privileges")
    List<Role> roles;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

}