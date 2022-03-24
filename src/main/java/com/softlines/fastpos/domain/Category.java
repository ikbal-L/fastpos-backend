package com.softlines.fastpos.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
public class Category extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank
    @Column(nullable = false,unique = true)
    String name;

    String backgroundString;

    @Column(unique = true)
    Integer rank;

    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "category_id")
    List<Product> products;



    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
    PrintingByCategoryConfiguration printingByCategoryConfiguration;

}
