package com.softlines.fastpos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

@SQLDelete(sql = "UPDATE Tables SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@javax.persistence.Table(name = "Tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int number;
    int seats;

    @Column(name = "isVirtual")
    boolean virtual;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

 }
