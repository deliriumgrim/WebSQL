package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.VerterEntity;
import edu.school21.info21.web.enums.CheckStatus;
import edu.school21.info21.web.repositories.data.VerterRepository;
import edu.school21.info21.web.services.data.VerterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VerterServiceImpl implements VerterService {

    private final VerterRepository verterRepository;

    @Autowired
    public VerterServiceImpl(VerterRepository verterRepository) {
        this.verterRepository = verterRepository;
    }

    @Override
    public List<VerterEntity> findAll() {
        return this.verterRepository.findAll();
    }

    @Override
    public void add(VerterEntity entity) {
        if (entity.getCheckId() == null
                || entity.getState() == null
                || (entity.getState() != CheckStatus.Success
                && entity.getState() != CheckStatus.Failure)
                || entity.getTime() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        System.out.println(entity.getState());
        this.verterRepository.save(entity);
    }

    @Override
    public List<VerterEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<VerterEntity> verter = this.verterRepository.findById(id);
        if (!verter.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Verter с ID: " + id + " - не существует");
        }
        return Collections.singletonList(verter.get());
    }

    @Override
    public void update(VerterEntity entity) {
        if (entity.getId() == null
                || entity.getCheckId() == null
                || entity.getState() == null
                || (entity.getState() != CheckStatus.Success
                && entity.getState() != CheckStatus.Failure)
                || entity.getTime() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<VerterEntity> verter = this.verterRepository.findById(entity.getId());
        if (!verter.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Verter с ID: " + entity.getId() + " - не существует");
        }
        this.verterRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<VerterEntity> verter = this.verterRepository.findById(id);
        if (!verter.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Verter с ID: " + id + " - не существует");
        }
        this.verterRepository.delete(verter.get());
    }
}
