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
@Table(name = "Tables")
public class Tables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int number;
    int seats;
    boolean isVirtual;
    @OneToMany(mappedBy = "table", fetch = FetchType.EAGER)
    List<Order> tableOrders;

//    List<Long> placeId;
//    Place place;

}
