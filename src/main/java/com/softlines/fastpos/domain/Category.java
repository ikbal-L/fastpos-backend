package com.softlines.fastpos.domain;

import javax.persistence.*;

import lombok.Data;

@Data

// still work
@Entity
public class Category {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String backgroundString;
    int rank;

    public Category(long id, String name, String backgroundString, int rank) {
        this.id = id;
        this.name = name;
        this.backgroundString = backgroundString;
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




}
