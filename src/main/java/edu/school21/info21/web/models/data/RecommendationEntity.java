package edu.school21.info21.web.models.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "recommendations")
public class RecommendationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "peer")
    private String peer;

    @Column(name = "recommended_peer")
    private String recommendedPeer;

}
