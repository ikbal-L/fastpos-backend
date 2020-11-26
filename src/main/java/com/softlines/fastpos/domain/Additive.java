package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Table(name = "additive")
public class Additive {
    // TODO Add Ingrediants to additive entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = MessageKeyConstants.ADDITIVE_DESCRIPTION_VALIDATION_ERROR)
    String description;

    @NotBlank(message = MessageKeyConstants.ADDITIVE_BACKGROUND_VALIDATION_ERROR)
    String backgroundString;

    @Column(nullable = false, unique = true)
    @Min(1)
    Integer rank;
}
