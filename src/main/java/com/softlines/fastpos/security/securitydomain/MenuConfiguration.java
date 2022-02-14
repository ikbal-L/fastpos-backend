package com.softlines.fastpos.security.securitydomain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
@Getter
@Setter
@Embeddable
public class MenuConfiguration {

    int productLayoutRows =4;
    int productLayoutColumns =5;

    int additiveLayoutRows =4;
    int additiveLayoutColumns =4;

    int categoryLayoutRows =3;
    int categoryLayoutColumns =5;
    int categoryPageCount=1;
}
