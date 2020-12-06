package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SQLDelete(sql = "UPDATE Person SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@DynamicUpdate
@MappedSuperclass
public class Person extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String name;

    String phoneNumber;

    String backgroundString;

    @Column(name="isActive")
    boolean active;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

}
