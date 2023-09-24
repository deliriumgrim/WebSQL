package edu.school21.info21.web.repositories.operations.impl;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.repositories.operations.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FunctionsRepositoryImpl implements FunctionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FunctionsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public QueryResultEntity callTransferredPointsTable() {
        String sql = "SELECT * " +
                "FROM fnc_transferred_points_table()";

        CallableStatementCallback<QueryResultEntity> callback = (cs) -> {
            cs.execute();
            return this.getQueryResult(cs);
        };

        return this.jdbcTemplate.execute(sql, callback);
    }

    @Override
    public QueryResultEntity callSuccessTasks() {
        String sql = "SELECT * " +
                "FROM fnc_success_tasks()";

        CallableStatementCallback<QueryResultEntity> callback = (cs) -> {
            cs.execute();
            return this.getQueryResult(cs);
        };

        return this.jdbcTemplate.execute(sql, callback);
    }

    @Override
    public QueryResultEntity callInOut(String date,
                                       Model model) {
        String sql = "SELECT * " +
                "FROM fnc_in_out(?)";

        CallableStatementCallback<QueryResultEntity> callback = (cs) -> {
            cs.execute();
            return this.getQueryResult(cs);
        };

        return this.jdbcTemplate.execute(sql, callback);
    }

    private QueryResultEntity getQueryResult(CallableStatement cs) {
        try {
            ResultSet resultSet = cs.getResultSet();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; ++i) {
                columns.add(resultSetMetaData.getColumnName(i));
            }

            List<List<String>> rows = new ArrayList<>();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; ++i) {
                    row.add(resultSet.getString(i));
                }
                rows.add(row);
            }
            return new QueryResultEntity(columns, rows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
