package com.softlines.fastpos.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
@Getter
@Setter
@Entity
public class PrintingByCategoryConfiguration extends PrintingConfiguration {
    @OneToMany(mappedBy = "printingByCategoryConfiguration")
    Set<Category> categories;
}
