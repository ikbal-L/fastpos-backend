package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@SQLDelete(sql = "UPDATE Product SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = MessageKeyConstants.PRODUCT_NAME_VALIDATION_ERROR)
    @Column(nullable = false,unique = true)
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