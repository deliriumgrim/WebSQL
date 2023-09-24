package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.P2pEntity;
import edu.school21.info21.web.repositories.data.P2pRepository;
import edu.school21.info21.web.services.data.P2pService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class P2pServiceImpl implements P2pService {

    private final P2pRepository p2pRepository;

    @Autowired
    public P2pServiceImpl(P2pRepository p2pRepository) {
        this.p2pRepository = p2pRepository;
    }

    @Override
    public List<P2pEntity> findAll() {
        return this.p2pRepository.findAll();
    }

    @Override
    public void add(P2pEntity entity) {
        if (entity.getCheckId() == null ||
                entity.getTime() == null ||
                entity.getCheckingPeer().isEmpty() ||
                entity.getCheckStatus() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.p2pRepository.save(entity);
    }

    @Override
    public List<P2pEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<P2pEntity> byId = this.p2pRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице P2P с ID: " + id + " - не существует");
        }
        return Collections.singletonList(byId.get());
    }

    @Override
    public void update(P2pEntity entity) {
        if (entity.getCheckId() == null ||
                entity.getTime() == null ||
                entity.getCheckingPeer().isEmpty() ||
                entity.getCheckStatus() == null ||
                entity.getId() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<P2pEntity> byId = this.p2pRepository.findById(entity.getId());
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице P2P с ID: " + entity.getId() + " - не существует");
        }
        this.p2pRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<P2pEntity> byId = this.p2pRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице P2P с ID: " + id + " - не существует");
        }
        this.p2pRepository.delete(byId.get());
    }
}
