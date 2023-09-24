package edu.school21.info21.web.repositories.operations;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

public interface ProcedureRepository {

    QueryResultEntity callBlockSuccess(String blockName);

    QueryResultEntity callChangePeerPointsOnAnotherFunc();

    QueryResultEntity callChecksBirthdayEntity();

    QueryResultEntity callFindBestDays(Integer num);

    QueryResultEntity callFindBestPeerOnXp();

    QueryResultEntity callFindBestPeer();

    QueryResultEntity callFindEarlyEntries();

    QueryResultEntity callFindPeersExitNDaysMTimes(Integer nDays, Integer nTimes);

    QueryResultEntity callFindPeersOnTasks(String firstName, String secondName, String thirdName);

    QueryResultEntity callMostChecksOnDay();

    QueryResultEntity callPeerPointsChange();

    QueryResultEntity callPeerTimeTrackingRepository(Integer numOfVisits, LocalTime time);

    void insertP2pCheck(String checkedName, String checkingName, String taskTitle, String checkStatus, LocalTime time);

    void insertVerterCheck(String checkedName, String taskTitle, String checkStatus, LocalTime time);

    QueryResultEntity callRecurFindTaskParents();

    QueryResultEntity callStatOfPeers(String firstBlock, String secondBlock);

    void importData(String tableName, String pathToFile);

    void exportData(String tableName, String pathToSave);

}
