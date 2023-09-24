package edu.school21.info21.web.repositories.data;

import edu.school21.info21.web.models.data.RecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<RecommendationEntity, Long> {
}
