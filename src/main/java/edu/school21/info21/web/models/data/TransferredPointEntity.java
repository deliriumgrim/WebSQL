package edu.school21.info21.web.models.data;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "transferred_points")
public class TransferredPointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "checking_peer")
    private String checkingPeer;

    @Column(name = "checked_peer")
    private String checkedPeer;

    @Column(name = "points_amount")
    private Integer pointsAmount;

}
