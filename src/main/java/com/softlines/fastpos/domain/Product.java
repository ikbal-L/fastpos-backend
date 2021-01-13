package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate

@SQLDelete(sql = "UPDATE Product SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = MessageKeyConstants.PRODUCT_NAME_VALIDATION_ERROR)
    @Column(nullable = false/*,unique = true*/)
    String name;


    @Column(nullable = false)
    double price;

    String unit;

    @Column(name = "isMuchInDemand")
    boolean muchInDemand;

    String type;

    int availableStock;

    String description;


    @Column(nullable = false)
    String backgroundString;

    @Column(name = "isPlatter")
    boolean platter;
    Integer rank;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "products_additives",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "additive_id"))
    List<Additive> additives;

    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    @Builder.Default
    @NotNull
    boolean deleted=false;

}