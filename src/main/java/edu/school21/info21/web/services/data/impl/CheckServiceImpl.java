package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.CheckEntity;
import edu.school21.info21.web.repositories.data.CheckRepository;
import edu.school21.info21.web.services.data.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CheckServiceImpl implements CheckService {

    private final CheckRepository checkRepository;

    @Autowired
    public CheckServiceImpl(CheckRepository checkRepository) {
        this.checkRepository = checkRepository;
    }

    @Override
    public List<CheckEntity> findAll() {
        return this.checkRepository.findAll();
    }

    @Override
    public void add(CheckEntity entity) {
        if (entity.getTask().isEmpty()
                || entity.getPeer().isEmpty()
                || entity.getDate() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.checkRepository.save(entity);
    }

    @Override
    public List<CheckEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<CheckEntity> byId = this.checkRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Checks с ID: " + id + " - не существует");
        }
        return Collections.singletonList(byId.get());
    }

    @Override
    public void update(CheckEntity entity) {
        if (entity.getId() == null
                || entity.getTask().isEmpty()
                || entity.getPeer().isEmpty()
                || entity.getDate() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<CheckEntity> byId = this.checkRepository.findById(entity.getId());
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Checks с ID: " + entity.getId() + " - не существует");
        }
        this.checkRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<CheckEntity> byId = this.checkRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Checks с ID: " + id + " - не существует");
        }
        this.checkRepository.delete(byId.get());
    }
}
