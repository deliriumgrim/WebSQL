package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.XpEntity;
import edu.school21.info21.web.repositories.data.XpRepository;
import edu.school21.info21.web.services.data.XpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class XpServiceImpl implements XpService {

    private final XpRepository xpRepository;

    @Autowired
    public XpServiceImpl(XpRepository xpRepository) {
        this.xpRepository = xpRepository;
    }

    @Override
    public List<XpEntity> findAll() {
        return this.xpRepository.findAll();
    }

    @Override
    public void add(XpEntity entity) {
        if (entity.getCheckId() == null
                || entity.getXpAmount() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.xpRepository.save(entity);
    }

    @Override
    public List<XpEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<XpEntity> xp = this.xpRepository.findById(id);
        if (!xp.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Xp с ID: " + id + " - не существует");
        }
        return Collections.singletonList(xp.get());
    }

    @Override
    public void update(XpEntity entity) {
        if (entity.getId() == null
                || entity.getCheckId() == null
                || entity.getXpAmount() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<XpEntity> xp = this.xpRepository.findById(entity.getId());
        if (!xp.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Xp с ID: " + entity.getId() + " - не существует");
        }
        this.xpRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<XpEntity> xp = this.xpRepository.findById(id);
        if (!xp.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Xp с ID: " + id + " - не существует");
        }
        this.xpRepository.delete(xp.get());
    }
}
