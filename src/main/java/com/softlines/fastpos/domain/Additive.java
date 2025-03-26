package com.softlines.fastpos.domain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@SQLDelete(sql = "UPDATE Additive SET deleted=true ,rank=null WHERE id=?")
@Where(clause = "deleted = false")
//
//@NaturalIdCache
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@Entity(name = "Additive")
@Table(name = "additive")
public class Additive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String description;

    @NotBlank(message = MessageKeyConstants.ADDITIVE_BACKGROUND_STRING_VALIDATION_ERROR)
    String backgroundString;

    @Column(nullable = true)
    @Min(1)
    Integer rank;

    boolean favorite = false;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

    String imageUrl;

}
