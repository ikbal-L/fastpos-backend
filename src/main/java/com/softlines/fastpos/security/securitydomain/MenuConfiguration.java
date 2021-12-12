package com.softlines.fastpos.security.securitydomain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
@Getter
@Setter
@Embeddable
public class MenuConfiguration {

    int productRows=4;
    int productColumns=5;

    int additiveRows=5;
    int additiveColumns=6;

    int categoryPageSize=4;
}
