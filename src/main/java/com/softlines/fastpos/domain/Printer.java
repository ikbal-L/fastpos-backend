package com.softlines.fastpos.domain;

import com.softlines.fastpos.domain.PrintingConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE Annex SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Printer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    @OneToMany
    @JoinColumn(name = "printer_id")
    Set<PrintingByCategoryConfiguration> printingConfigurations;
    boolean deleted= false;
}
