package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.TimeTrackingEntity;
import edu.school21.info21.web.repositories.data.TimeTrackingRepository;
import edu.school21.info21.web.services.data.TimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TimeTrackingServiceImpl implements TimeTrackingService {

    private final TimeTrackingRepository timeTrackingRepository;

    @Autowired
    public TimeTrackingServiceImpl(TimeTrackingRepository timeTrackingRepository) {
        this.timeTrackingRepository = timeTrackingRepository;
    }

    @Override
    public List<TimeTrackingEntity> findAll() {
        return this.timeTrackingRepository.findAll();
    }

    @Override
    public void add(TimeTrackingEntity entity) {
        if (entity.getPeer().isEmpty()
                || entity.getLocalDate() == null
                || entity.getLocalTime() == null
                || entity.getState() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.timeTrackingRepository.save(entity);
    }

    @Override
    public List<TimeTrackingEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TimeTrackingEntity> timeTracking = this.timeTrackingRepository.findById(id);
        if (!timeTracking.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Time_Tracking с ID: " + id + " - не существует");
        }
        return Collections.singletonList(timeTracking.get());
    }

    @Override
    public void update(TimeTrackingEntity entity) {
        if (entity.getId() == null
                || entity.getPeer().isEmpty()
                || entity.getLocalDate() == null
                || entity.getLocalTime() == null
                || entity.getState() == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TimeTrackingEntity> timeTracking
                = this.timeTrackingRepository.findById(entity.getId());
        if (!timeTracking.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Time_Tracking с ID: " + entity.getId() + " - не существует");
        }
        this.timeTrackingRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<TimeTrackingEntity> timeTracking = this.timeTrackingRepository.findById(id);
        if (!timeTracking.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Time_Tracking с ID: " + id + " - не существует");
        }
        this.timeTrackingRepository.delete(timeTracking.get());
    }
}
