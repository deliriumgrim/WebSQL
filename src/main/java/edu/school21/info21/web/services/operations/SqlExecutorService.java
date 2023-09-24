package edu.school21.info21.web.services.operations;

import edu.school21.info21.web.models.operations.QueryResultEntity;

public interface SqlExecutorService {

    QueryResultEntity executeQuery(String query);

}
