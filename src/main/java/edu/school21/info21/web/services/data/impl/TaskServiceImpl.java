package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataAlreadyExistsException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.TaskEntity;
import edu.school21.info21.web.repositories.data.TaskRepository;
import edu.school21.info21.web.services.data.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskEntity> findAll() {
        return this.taskRepository.findAll();
    }

    @Override
    public void add(TaskEntity entity) {
        if (entity.getTitle().isEmpty()
                || entity.getMaxXp() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми, кроме поля Parent task");
        }
        if (entity.getParentTask().isEmpty()) {
            entity.setParentTask(null);
        }
        Optional<TaskEntity> task = this.taskRepository.findById(entity.getTitle());
        if (task.isPresent()) {
            throw new DataAlreadyExistsException("Запись в таблице Tasks с Title: " + entity.getTitle() + " - существует");
        }
        this.taskRepository.save(entity);
    }

    @Override
    public List<TaskEntity> findById(String id) {
        if (id.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TaskEntity> task = this.taskRepository.findById(id);
        if (!task.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Tasks с Title: " + id + " - не существует");
        }
        return Collections.singletonList(task.get());
    }

    @Override
    public void update(TaskEntity entity) {
        if (entity.getTitle().isEmpty()
                || entity.getParentTask().isEmpty()
                || entity.getMaxXp() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TaskEntity> task = this.taskRepository.findById(entity.getTitle());
        if (!task.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Tasks с Title: " + entity.getTitle() + " - не существует");
        }
        this.taskRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (id.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TaskEntity> task = this.taskRepository.findById(id);
        if (!task.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Tasks с Title: " + id + " - не существует");
        }
        this.taskRepository.delete(task.get());
    }
}
