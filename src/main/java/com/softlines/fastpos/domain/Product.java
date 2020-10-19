package com.softlines.fastpos.domain;

import javax.persistence.*;
import java.util.List;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    long id;
    String name;
    double price;
    String unit;
    boolean isMuchInDemand;
    String color;
    int categorieId;
    @ElementCollection
    List<Long> idAdditives;
    String type;
    int availableStock;
    String description;
    boolean isSelected;
    String backgroundString;
    boolean isPlatter = false;
    int rank;

    @ElementCollection
    List<Additive> Additives;



    public Product() {

    }

    public Product(long id, String name, double price, String unit, boolean isMuchInDemand, List<Long> idAdditives, String color, int categorieId, String type, int availableStock, String description, boolean isSelected, String backgroundString, boolean isPlatter, int rank) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.isMuchInDemand = isMuchInDemand;
        this.color = color;
        this.idAdditives = idAdditives;
        this.categorieId = categorieId;
        this.type = type;
        this.availableStock = availableStock;
        this.description = description;
        this.isSelected = isSelected;
        this.backgroundString = backgroundString;
        this.isPlatter = isPlatter;
        this.rank = rank;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isMuchInDemand() {
        return isMuchInDemand;
    }

    public void setMuchInDemand(boolean muchInDemand) {
        isMuchInDemand = muchInDemand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getBackgroundString() {
        return backgroundString;
    }

    public void setBackgroundString(String backgroundString) {
        this.backgroundString = backgroundString;
    }

    public boolean isPlatter() {
        return isPlatter;
    }

    public void setPlatter(boolean platter) {
        isPlatter = platter;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    public List<Long> getIdAdditives() {
        return idAdditives;
    }

    public void setIdAdditives(List<Long> idAdditives) {
        this.idAdditives = idAdditives;
    }

    public List<com.softlines.fastpos.domain.Additive> getAdditive() {
        return Additives;
    }
    public void setAdditive(List<com.softlines.fastpos.domain.Additive> additive) {
        Additives = additive;
    }
}