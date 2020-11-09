package com.softlines.fastpos.domain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "additive")
public class Additive {
    // TODO Add Ingrediants to additive entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "description",nullable = false,unique = true)
    @NotBlank(message = MessageKeyConstants.ADDITIVE_DESCRIPTION_VALIDATION_ERROR)
    String description ;
    @NotBlank(message = MessageKeyConstants.ADDITIVE_BACKGROUND_VALIDATION_ERROR)
    String backgroundString;
    int rank;
}
