package edu.school21.info21.web.repositories.data;

import edu.school21.info21.web.models.data.TransferredPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferredPointRepository extends JpaRepository<TransferredPointEntity, Long> {
}
