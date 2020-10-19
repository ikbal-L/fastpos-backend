package com.softlines.fastpos.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDto {
    long id;
    String name;
    double price;
    String unit;
    boolean isMuchInDemand;
    String color;
    int categorieId;
    List<Long> idAdditive;
    String type;
    int availableStock;
    String description;
    boolean isSelected;
    String backgroundString;
    boolean isPlatter = false;
    int rank;


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

    public List<Long> getIdAdditive() {
        return idAdditive;
    }

    public void setIdAdditive(List<Long> idAdditive) {
        this.idAdditive = idAdditive;
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



    public ProductDto(){}
}
