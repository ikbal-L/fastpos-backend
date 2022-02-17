package com.softlines.fastpos.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class PrintingByCategoryConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @ManyToOne
    Printer printer;


    @OneToMany(mappedBy = "printingByCategoryConfiguration",fetch = FetchType.EAGER,cascade = {CascadeType.MERGE})
    Set<Category> categories;
}
