package edu.school21.info21.web.services.operations;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import org.springframework.ui.Model;

public interface FunctionService {

    QueryResultEntity transferredPointsReadableView(Model model);

    QueryResultEntity successTasks(Model model);

    QueryResultEntity notGoOutPeers(String date, Model model);

}
