package com.softlines.fastpos.domain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@SuperBuilder
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"rank", "category_id"}))
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = MessageKeyConstants.PRODUCT_NAME_VALIDATION_ERROR)
    @Column(nullable = false, unique = true)
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
//    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(
            name = "products_additives",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "additive_id"))
    List<Additive> additives;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;



    String imageUrl;

}