package com.softlines.fastpos.domain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;


@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@SQLDelete(sql = "UPDATE Additive SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@NaturalIdCache
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@javax.persistence.Table(name = "additive")
@Entity(name = "Additive")
public class Additive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true)
    String description;

    @NotBlank(message = MessageKeyConstants.ADDITIVE_BACKGROUND_STRING_VALIDATION_ERROR)
    String backgroundString;

    @Column(nullable = false, unique = true)
    @Min(1)
    Integer rank;

//    @NotNull
//    Date timestamp;
//
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    AdditiveSate sate;


    @OneToMany(mappedBy = "additive", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItemAdditive> orderItemAdditives;

    @Builder.Default
    @NotNull
    private boolean deleted = false;



}
