package edu.school21.info21.web.repositories.operations.impl;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.repositories.operations.ProcedureRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcedureRepositoryImpl implements ProcedureRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProcedureRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public QueryResultEntity callBlockSuccess(String blockName) {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.setString(1, blockName);
                    cs.registerOutParameter(2, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 2);
                };
        return this.jdbcTemplate.execute("CALL block_success(?, ?)", callback);
    }

    @Override
    public QueryResultEntity callChangePeerPointsOnAnotherFunc() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL change_peer_points_on_another_func(?)", callback);
    }

    @Override
    public QueryResultEntity callChecksBirthdayEntity() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL checks_birthday(?)", callback);
    }

    @Override
    public QueryResultEntity callFindBestDays(Integer num) {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.setInt(1, num);
                    cs.registerOutParameter(2, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 2);
                };
        return this.jdbcTemplate.execute("CALL find_best_days(?, ?)", callback);
    }

    @Override
    public QueryResultEntity callFindBestPeerOnXp() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL find_best_peer_on_xp(?)", callback);
    }

    @Override
    public QueryResultEntity callFindBestPeer() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL find_best_peer(?)", callback);
    }

    @Override
    public QueryResultEntity callFindEarlyEntries() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL find_early_entries(?)", callback);
    }

    @Override
    public QueryResultEntity callFindPeersExitNDaysMTimes(Integer nDays,
                                                          Integer nTimes) {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.setInt(1, nDays);
                    cs.setInt(2, nTimes);
                    cs.registerOutParameter(3, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 3);
                };
        return this.jdbcTemplate.execute("CALL find_peers_exit_n_days_m_times(?, ?, ?)", callback);
    }

    @Override
    public QueryResultEntity callFindPeersOnTasks(String firstName,
                                                  String secondName,
                                                  String thirdName) {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.setString(1, firstName);
                    cs.setString(2, secondName);
                    cs.setString(3, thirdName);
                    cs.registerOutParameter(4, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 4);
                };
        return this.jdbcTemplate.execute("CALL find_peers_on_tasks(?, ?, ?, ?)", callback);
    }

    @Override
    public QueryResultEntity callMostChecksOnDay() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL most_checks_on_day(?)", callback);
    }

    @Override
    public QueryResultEntity callPeerPointsChange() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL change_peer_points(?)", callback);
    }

    @Override
    public QueryResultEntity callPeerTimeTrackingRepository(Integer numOfVisits,
                                                            LocalTime time) {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.setInt(1, numOfVisits);
                    cs.setTime(2, Time.valueOf(time));
                    cs.registerOutParameter(3, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 3);
                };
        return this.jdbcTemplate.execute("CALL peer_time_track(?, ?, ?)", callback);
    }

    @Override
    public void insertP2pCheck(String checkedName,
                               String checkingName,
                               String taskTitle,
                               String checkStatus,
                               LocalTime time) {
        this.jdbcTemplate.update("CALL insert_p2p_check(?, ?, ?, ?::check_status, ?)",
                checkedName,
                checkingName,
                taskTitle,
                checkStatus,
                Time.valueOf(time));
    }

    @Override
    public void insertVerterCheck(String checkedName,
                                  String taskTitle,
                                  String checkStatus,
                                  LocalTime time) {
        this.jdbcTemplate.update("CALL insert_verter_check(?, ?, ?::check_status, ?)",
                checkedName,
                taskTitle,
                checkStatus,
                Time.valueOf(time));
    }

    @Override
    public QueryResultEntity callRecurFindTaskParents() {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 1);
                };
        return this.jdbcTemplate.execute("CALL recur_find_task_parents(?)", callback);
    }

    @Override
    public QueryResultEntity callStatOfPeers(String firstBlock,
                                             String secondBlock) {
        CallableStatementCallback<QueryResultEntity> callback =
                cs -> {
                    cs.setString(1, firstBlock);
                    cs.setString(2, secondBlock);
                    cs.registerOutParameter(3, Types.REF_CURSOR);
                    cs.execute();
                    return this.getQueryResult(cs, 3);
                };
        return this.jdbcTemplate.execute("CALL stat_of_peers(?, ?, ?)", callback);
    }

    @Override
    public void importData(String tableName,
                           String pathToFile) {
        try {
            this.jdbcTemplate.update("CALL import(?, ?)",
                    tableName,
                    pathToFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportData(String tableName,
                           String pathToSave) {
        this.jdbcTemplate.update("CALL export(?, ?)",
                tableName,
                pathToSave);
    }

    private QueryResultEntity getQueryResult(CallableStatement cs,
                                             Integer outParam) {
        try {
            ResultSet resultSet = (ResultSet) cs.getObject(outParam);
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
