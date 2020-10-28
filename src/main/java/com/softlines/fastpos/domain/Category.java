package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","products"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String backgroundString;
    int rank;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    List<Product> products;


}
