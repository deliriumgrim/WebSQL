package edu.school21.info21.web.models.data;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "time_tracking")
public class TimeTrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "peer")
    private String peer;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    @Column(name = "time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime localTime;

    @Column(name = "state")
    private Integer state;

}
