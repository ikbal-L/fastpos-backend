package com.softlines.fastpos.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.List;


@Data
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
