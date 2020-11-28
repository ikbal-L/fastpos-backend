package com.softlines.fastpos.domain;

import com.softlines.fastpos.jwtsecurity.securitydomain.Session;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

//@Entity
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity {


    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;


    @CreatedBy
    @Column(name = "creation_session_id",nullable = false,updatable = false)
    private String creationSessionId;

    @LastModifiedBy
    @Column(name = "modification_session_id")
    private String modificationSessionId;



}
