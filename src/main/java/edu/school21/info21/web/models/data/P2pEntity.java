package edu.school21.info21.web.models.data;

import edu.school21.info21.web.enums.CheckStatus;
import edu.school21.info21.web.enums.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "p2p")
@TypeDef(
        name = "check_status",
        typeClass = PostgreSQLEnumType.class
)
public class P2pEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "check_id")
    private Long checkId;

    @Column(name = "checking_peer")
    private String checkingPeer;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Type(type = "check_status")
    private CheckStatus checkStatus;

    @Column(name = "time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
}
