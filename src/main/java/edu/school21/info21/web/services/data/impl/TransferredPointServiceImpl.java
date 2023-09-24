package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.TransferredPointEntity;
import edu.school21.info21.web.repositories.data.TransferredPointRepository;
import edu.school21.info21.web.services.data.TransferredPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TransferredPointServiceImpl implements TransferredPointService {

    private final TransferredPointRepository transferredPointRepository;

    @Autowired
    public TransferredPointServiceImpl(TransferredPointRepository transferredPointRepository) {
        this.transferredPointRepository = transferredPointRepository;
    }

    @Override
    public List<TransferredPointEntity> findAll() {
        return this.transferredPointRepository.findAll();
    }

    @Override
    public void add(TransferredPointEntity entity) {
        if (entity.getCheckingPeer().isEmpty()
                || entity.getCheckedPeer().isEmpty()
                || entity.getPointsAmount() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.transferredPointRepository.save(entity);
    }

    @Override
    public List<TransferredPointEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TransferredPointEntity> transferredPoint
                = this.transferredPointRepository.findById(id);
        if (!transferredPoint.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Transferred_Points с ID: " + id + " - не существует");
        }
        return Collections.singletonList(transferredPoint.get());
    }

    @Override
    public void update(TransferredPointEntity entity) {
        if (entity.getId() == null
                || entity.getCheckingPeer().isEmpty()
                || entity.getCheckedPeer().isEmpty()
                || entity.getPointsAmount() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TransferredPointEntity> transferredPoint
                = this.transferredPointRepository.findById(entity.getId());
        if (!transferredPoint.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Transferred_Points с ID: " + entity.getId() + " - не существует");
        }
        this.transferredPointRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TransferredPointEntity> transferredPoint
                = this.transferredPointRepository.findById(id);
        if (!transferredPoint.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Transferred_Points с ID: " + id + " - не существует");
        }
        this.transferredPointRepository.delete(transferredPoint.get());
    }
}
