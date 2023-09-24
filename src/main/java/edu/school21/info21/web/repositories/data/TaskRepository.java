package edu.school21.info21.web.repositories.data;

import edu.school21.info21.web.models.data.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, String> {
}
