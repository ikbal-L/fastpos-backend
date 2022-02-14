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

@SQLDelete(sql = "UPDATE Category SET deleted=true ,rank=null WHERE id=?")
@Where(clause = "deleted = false")
@Entity
public class Category extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank
    @Column(nullable = false,unique = true)
    String name;

    String backgroundString;
    Integer rank;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    List<Product> products;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    PrintingByCategoryConfiguration printingByCategoryConfiguration;

}
