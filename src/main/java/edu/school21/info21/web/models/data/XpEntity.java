package edu.school21.info21.web.models.data;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "xp")
public class XpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "check_id")
    private Long checkId;

    @Column(name = "xp_amount")
    private Integer xpAmount;

}
