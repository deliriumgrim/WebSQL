package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataAlreadyExistsException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.PeerEntity;
import edu.school21.info21.web.repositories.data.PeerRepository;
import edu.school21.info21.web.services.data.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PeerServiceImpl implements PeerService {

    private final PeerRepository peerRepository;

    @Autowired
    PeerServiceImpl(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    @Override
    public List<PeerEntity> findAll() {
        return this.peerRepository.findAll();

    }

    @Override
    public void add(PeerEntity entity) {
        if (entity.getNickname().isEmpty()
                || entity.getBirthday() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<PeerEntity> byId = this.peerRepository.findById(entity.getNickname());
        if (byId.isPresent()) {
            throw new DataAlreadyExistsException("Запись в таблице Peers с Nickname: " + entity.getNickname() + " - существует");
        }
        this.peerRepository.save(entity);
    }

    @Override
    public List<PeerEntity> findById(String id) {
        if (id.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<PeerEntity> byId = this.peerRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Peers с Nickname: " + id + " - не существует");
        }
        return Collections.singletonList(byId.get());
    }

    @Override
    public void update(PeerEntity entity) {
        if (entity.getNickname().isEmpty()
                || entity.getBirthday() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<PeerEntity> byId = this.peerRepository.findById(entity.getNickname());
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Peers с Nickname: " + entity.getNickname() + " - не существует");
        }
        this.peerRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (id.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<PeerEntity> byId = this.peerRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Peers с Nickname: " + id + " - не существует");
        }
        this.peerRepository.delete(byId.get());
    }
}
