package com.softlines.fastpos.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@SuperBuilder
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Deliveryman SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Data
public class Deliveryman extends  Person{

    double balance;

}
