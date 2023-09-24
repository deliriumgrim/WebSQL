package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.FriendEntity;
import edu.school21.info21.web.repositories.data.FriendRepository;
import edu.school21.info21.web.services.data.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public List<FriendEntity> findAll() {
        return this.friendRepository.findAll();
    }

    @Override
    public void add(FriendEntity entity) {
        if (entity.getFirstPeer().isEmpty()
                || entity.getSecondPeer().isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.friendRepository.save(entity);
    }

    @Override
    public List<FriendEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<FriendEntity> byId = this.friendRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Friends с ID: " + id + " - не существует");
        }
        return Collections.singletonList(byId.get());
    }

    @Override
    public void update(FriendEntity entity) {
        if (entity.getId() == null
                || entity.getFirstPeer().isEmpty()
                || entity.getSecondPeer().isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<FriendEntity> byId = this.friendRepository.findById(entity.getId());
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Friends с ID: " + entity.getId() + " - не существует");
        }
        this.friendRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<FriendEntity> byId = this.friendRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Friends с ID: " + id + " - не существует");
        }
        this.friendRepository.delete(byId.get());
    }
}
