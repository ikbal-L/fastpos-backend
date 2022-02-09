package com.softlines.fastpos.borne.dto;

import com.softlines.fastpos.borne.PageItemType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageItem {
    long id;
    String title;
    double prix1;
    double prix2;
    boolean disponible;
    PageItemType pageItemType;
    String description;
    boolean uniqueChoice;
    String image;
    double tva1;
    double tva2;
    double tva3;
    PageModel nextPage;
    int quantity;
    boolean isSelected;
}
