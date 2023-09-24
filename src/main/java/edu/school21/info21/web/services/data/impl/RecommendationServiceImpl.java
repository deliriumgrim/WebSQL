package edu.school21.info21.web.services.data.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.DataNotExistsException;
import edu.school21.info21.web.models.data.RecommendationEntity;
import edu.school21.info21.web.repositories.data.RecommendationRepository;
import edu.school21.info21.web.services.data.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public List<RecommendationEntity> findAll() {
        return this.recommendationRepository.findAll();
    }

    @Override
    public void add(RecommendationEntity entity) {
        if (entity.getRecommendedPeer().isEmpty()
                || entity.getPeer().isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.recommendationRepository.save(entity);
    }

    @Override
    public List<RecommendationEntity> findById(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<RecommendationEntity> byId = this.recommendationRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Recommendations с ID: " + id + " - не существует");
        }
        return Collections.singletonList(byId.get());
    }

    @Override
    public void update(RecommendationEntity entity) {
        if (entity.getId() == null
                || entity.getPeer().isEmpty()
                || entity.getRecommendedPeer().isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<RecommendationEntity> byId
                = this.recommendationRepository.findById(entity.getId());
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Recommendations с ID: " + entity.getId() + " - не существует");
        }
        this.recommendationRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        Optional<RecommendationEntity> byId = this.recommendationRepository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotExistsException("Запись в таблице Recommendations с ID: " + id + " - не существует");
        }
        this.recommendationRepository.delete(byId.get());
    }
}
