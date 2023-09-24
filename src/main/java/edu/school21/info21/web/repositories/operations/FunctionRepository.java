package edu.school21.info21.web.repositories.operations;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import org.springframework.ui.Model;

public interface FunctionRepository {

    QueryResultEntity callTransferredPointsTable();

    QueryResultEntity callSuccessTasks();

    QueryResultEntity callInOut(String date, Model model);

}
