package com.softlines.fastpos.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false,unique = true)
    String name;
    String backgroundString;
    Integer rank;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    List<Product> products;


}
