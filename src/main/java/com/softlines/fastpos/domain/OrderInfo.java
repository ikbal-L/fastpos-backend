package com.softlines.fastpos.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int orderCount;
    @Column(nullable = false)
    LocalDate date;

    public void incrementOrderCount(){
        orderCount++;
    }
}
