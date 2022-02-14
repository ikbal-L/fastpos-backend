package com.softlines.fastpos.security.securitydomain;

import com.softlines.fastpos.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
public class AnnexConfiguration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "productLayoutRows", column = @Column(name = "product_menu_layout_rows")),
            @AttributeOverride( name = "productLayoutColumns", column = @Column(name = "product_menu_layout_columns")),
            @AttributeOverride( name = "additiveLayoutRows", column = @Column(name = "additive_menu_layout_rows")),
            @AttributeOverride( name = "additiveLayoutColumns", column = @Column(name = "additive_menu_layout_columns")),
            @AttributeOverride( name = "categoryPageCount", column = @Column(name = "category_menu_page_count")),
    })
    MenuConfiguration menu;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "configuration")
    Annex annex;

}
