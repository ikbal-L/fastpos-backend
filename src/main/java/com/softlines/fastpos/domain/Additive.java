package com.softlines.fastpos.domain;

import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Additive SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
@javax.persistence.Table(name = "additive")
public class Additive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true)
    String description;

    @NotBlank(message = MessageKeyConstants.ADDITIVE_BACKGROUND_STRING_VALIDATION_ERROR)
    String backgroundString;

    @Column(nullable = false, unique = true)
    @Min(1)
    Integer rank;

//    @NotNull
//    Date timestamp;
//
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    AdditiveSate sate;

    @OneToMany(mappedBy = "additive",cascade = {CascadeType.ALL})
    List<OrderItemAdditive> orderItemAdditives;

    @Builder.Default
    @NotNull
    private boolean deleted = false;
}
