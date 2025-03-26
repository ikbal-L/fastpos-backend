package com.softlines.fastpos.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyncEntity extends BaseEntity {

    @Column(name = "is_locked")
    boolean locked;

    @Column(name = "locked_by")
    String lockedBy;
}
