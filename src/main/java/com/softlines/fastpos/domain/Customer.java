package com.softlines.fastpos.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="customer_phone_numbers", joinColumns=@JoinColumn(name="customer_id"))
    @Column(name="customer_phone_number")
    Set<String> phoneNumbers;

    float debit;
    @Builder.Default
    @NotNull
    private boolean deleted=false;

    @OneToMany(mappedBy ="customer" ,fetch = FetchType.LAZY)
    Set<Order> orders;
}
