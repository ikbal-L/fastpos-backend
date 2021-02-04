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
@SQLDelete(sql = "UPDATE Customer SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@Entity

public class Customer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String name;

    String mobile;

    @Builder.Default
    @NotNull
    private boolean deleted=false;

    @OneToMany(mappedBy ="customer" ,fetch = FetchType.LAZY)
    List<Order> orders;
}
