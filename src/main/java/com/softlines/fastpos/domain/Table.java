package com.softlines.fastpos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@javax.persistence.Table(name = "Table")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int number;
    int seats;
    @Column(name = "isVirtual")
    boolean virtual;
    @OneToMany(mappedBy = "table", fetch = FetchType.LAZY)
    List<Order> tableOrders;


}
