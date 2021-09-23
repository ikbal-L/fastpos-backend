package com.softlines.fastpos.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyncEntity  extends BaseEntity{

    @Column(name = "is_locked")
    @Builder.Default
    boolean isLocked = false;
}
