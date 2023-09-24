package edu.school21.info21.web.models.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "friends")
@Data
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "peer_1")
    private String firstPeer;

    @Column(name = "peer_2")
    private String secondPeer;
}
