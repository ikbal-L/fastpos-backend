package com.softlines.fastpos.jwtsecurity.securitydomain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = MessageKeyConstants.PRIVILEGE_NAME_VALIDATION_ERROR)
    @Column(name = "name")
    private String name;

}