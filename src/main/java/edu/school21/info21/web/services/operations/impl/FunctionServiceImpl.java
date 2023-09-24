package edu.school21.info21.web.services.operations.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.repositories.operations.FunctionRepository;
import edu.school21.info21.web.services.operations.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;

@Service
public class FunctionServiceImpl implements FunctionService {

    private final FunctionRepository functionRepository;

    @Autowired
    public FunctionServiceImpl(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    @Transactional
    @Override
    public QueryResultEntity transferredPointsReadableView(Model model) {
        return this.functionRepository.callTransferredPointsTable();
    }

    @Transactional
    @Override
    public QueryResultEntity successTasks(Model model) {
        return this.functionRepository.callSuccessTasks();
    }

    @Transactional
    @Override
    public QueryResultEntity notGoOutPeers(String date,
                                           Model model) {
        if (date.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.functionRepository.callInOut(date, model);
    }
}
