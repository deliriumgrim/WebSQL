package edu.school21.info21.web.repositories.operations.impl;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.repositories.operations.SqlExecutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SqlExecutorRepositoryImpl implements SqlExecutorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlExecutorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void executeUpdate(String query) {
        this.jdbcTemplate.update(query);
    }

    @Override
    public QueryResultEntity executeQuery(String query) {
        CallableStatementCallback<QueryResultEntity> callback = (cs) -> {
            cs.execute();
            return this.getQueryResult(cs);
        };

        return this.jdbcTemplate.execute(query, callback);
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
