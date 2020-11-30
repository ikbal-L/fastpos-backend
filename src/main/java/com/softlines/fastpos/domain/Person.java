package com.softlines.fastpos.domain;


import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

@SQLDelete(sql = "UPDATE Person SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@MappedSuperclass
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String name;

    String phoneNumber;

    String backgroundString;

    @Column(name="isActive")
    boolean active;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

}
