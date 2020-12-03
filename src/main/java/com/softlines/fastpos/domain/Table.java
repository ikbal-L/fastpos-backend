package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@SuperBuilder
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Entity

@SQLDelete(sql = "UPDATE Tables SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

@javax.persistence.Table(name = "Tables")
public class Table extends BaseEntity{

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
