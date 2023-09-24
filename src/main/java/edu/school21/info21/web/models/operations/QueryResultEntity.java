package edu.school21.info21.web.models.operations;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryResultEntity {

    private List<String> columns;
    private List<List<String>> rows;

}
