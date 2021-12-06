package com.softlines.fastpos.security.securitydomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "session")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Session SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Session {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private User user ;

    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;

    private Agent agent ;
    private String ipAddress;

    @Builder.Default
    @NotNull
    private boolean deleted = false;

}
