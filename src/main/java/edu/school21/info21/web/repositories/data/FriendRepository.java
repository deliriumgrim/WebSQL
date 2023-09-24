package edu.school21.info21.web.repositories.data;

import edu.school21.info21.web.models.data.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
}
