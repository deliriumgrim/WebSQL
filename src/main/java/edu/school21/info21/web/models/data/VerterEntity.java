package edu.school21.info21.web.models.data;

import edu.school21.info21.web.enums.CheckStatus;
import edu.school21.info21.web.enums.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "verter")
@TypeDef(
        name = "check_status",
        typeClass = PostgreSQLEnumType.class
)
public class VerterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "check_id")
    private Long checkId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Type(type = "check_status")
    private CheckStatus state;

    @Column(name = "time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

}
