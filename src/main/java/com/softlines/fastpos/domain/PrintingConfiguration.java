package com.softlines.fastpos.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
//
//@Getter
//@Setter
//@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PrintingConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    String name;
    @ManyToOne
    Printer printer;
}
