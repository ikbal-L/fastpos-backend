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
            @AttributeOverride( name = "productRows", column = @Column(name = "product_menu_rows")),
            @AttributeOverride( name = "productColumns", column = @Column(name = "product_menu_columns")),
            @AttributeOverride( name = "additiveRows", column = @Column(name = "additive_menu_rows")),
            @AttributeOverride( name = "additiveColumns", column = @Column(name = "additive_menu_columns")),
            @AttributeOverride( name = "categoryPageSize", column = @Column(name = "category_menu_page_size")),
    })
    MenuConfiguration menu;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "configuration")
    Annex annex;

}
