package edu.school21.info21.web.repositories.data;

import edu.school21.info21.web.models.data.TimeTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTrackingRepository extends JpaRepository<TimeTrackingEntity, Long> {
}
