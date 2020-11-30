package com.softlines.fastpos.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Waiter SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Waiter extends  Person{


}
