package com.softlines.fastpos.borne.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class PageModel {
    String pageName;
    String family;
    String title;
    String subtitle;
    int nbrUnitMax;
    int maxGlobal;
    int nbrGratuit;
    int nbrColonnes;
    int nbrMinObligatoir;
    PageModel nextPage;
    List<PageItem> listItems;
}
