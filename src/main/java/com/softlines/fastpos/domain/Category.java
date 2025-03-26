package com.softlines.fastpos.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
public class Category extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(nullable = false,unique = true)
    String name;

    String backgroundString;

    @Column(unique = true)
    Integer rank;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    List<Product> products;



    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
    PrintingByCategoryConfiguration printingByCategoryConfiguration;
    @PreRemove
    public void onDeleteSetNull(){
        if (products!= null){
            products.forEach(product -> {
                product.setCategory(null);
                product.setRank(null);
            });
        }
    }
    //Done Issue: conflict between the rank check and permutation endpoint
    @PreUpdate
    public void onCategoryInactive(){
        if (rank == null&& !beingPermutated && id!= null && products!= null){
            products.forEach(product -> {
                product.setCategory(null);
                product.setRank(null);
            });
        }

    }
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private  boolean beingPermutated = false;

    public void setRankNullOnPermutation(){
        rank = null;
        beingPermutated = true;
    }

}
