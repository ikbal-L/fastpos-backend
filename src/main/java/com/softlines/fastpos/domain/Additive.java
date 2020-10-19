package com.softlines.fastpos.domain;


import javax.persistence.*;
import java.util.List;


// still work
@Embeddable
@Entity
public class Additive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String description;
    @ElementCollection
    List<Integer> idIngrediants;
    String backgroundString;
    int rank;

    public Additive() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getIdIngrediants() {
        return idIngrediants;
    }

    public void setIdIngrediants(List<Integer> idIngrediants) {
        this.idIngrediants = idIngrediants;
    }

    public String getBackgroundString() {
        return backgroundString;
    }

    public void setBackgroundString(String backgroundString) {
        this.backgroundString = backgroundString;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    public Additive(long id, String description, List<Integer> idIngrediants, String backgroundString, int rank) {
        this.id = id;
        this.description = description;
        this.idIngrediants = idIngrediants;
        this.backgroundString = backgroundString;
        this.rank = rank;
    }


}
