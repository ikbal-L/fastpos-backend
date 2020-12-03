package com.softlines.fastpos.domain;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.Entity;

@SuperBuilder
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Deliveryman SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")

public class Deliveryman extends  Person{
}
