package edu.school21.info21.web.repositories.operations;

import edu.school21.info21.web.models.operations.QueryResultEntity;

public interface SqlExecutorRepository {

    QueryResultEntity executeQuery(String query);

    void executeUpdate(String query);


}
