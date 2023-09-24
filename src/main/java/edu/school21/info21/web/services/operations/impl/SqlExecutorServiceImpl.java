package edu.school21.info21.web.services.operations.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.exceptions.BadSqlOperationException;
import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.repositories.operations.SqlExecutorRepository;
import edu.school21.info21.web.services.operations.SqlExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SqlExecutorServiceImpl implements SqlExecutorService {

    private final SqlExecutorRepository sqlExecutorRepository;

    @Autowired
    public SqlExecutorServiceImpl(SqlExecutorRepository sqlExecutorRepository) {
        this.sqlExecutorRepository = sqlExecutorRepository;
    }

    @Transactional
    @Override
    public QueryResultEntity executeQuery(String query) {
        if (query.isEmpty()) {
            throw new BadInputException("Поле ввода не может быть пустым");
        }
        QueryResultEntity sqlExecutor = null;
        if (query.toUpperCase().startsWith("INSERT")
                || query.toUpperCase().startsWith("UPDATE")
                || query.toUpperCase().startsWith("DELETE")) {
            this.sqlExecutorRepository.executeUpdate(query);
        } else if (query.toUpperCase().startsWith("SELECT")) {
            sqlExecutor = this.sqlExecutorRepository.executeQuery(query);
        } else {
            throw new BadSqlOperationException("Синтаксис не распознан." +
                    "\n Допустимые операции: SELECT, UPDATE, DELETE, INSERT");
        }
        return (sqlExecutor);
    }
}
