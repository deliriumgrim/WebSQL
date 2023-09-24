package edu.school21.info21.web.services.operations;

import edu.school21.info21.web.models.operations.QueryResultEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

public interface ProcedureService {

    void insertP2pCheck(String checkedName, String checkingName, String taskTitle, String checkStatus, LocalTime time);

    void insertVerter(String checkedName, String taskTitle, String checkStatus, LocalTime time);

    QueryResultEntity peerPointsChanges(Model model);

    QueryResultEntity changePeerPointsOnAnotherFunc(Model model);

    QueryResultEntity mostChecksOnDay(Model model);

    QueryResultEntity blockSuccess(Model model, String blockName);

    QueryResultEntity findBestPeer(Model model);

    QueryResultEntity statOfPeers(Model model, String firstBlock, String secondBlock);

    QueryResultEntity checksBirthday(Model model);

    QueryResultEntity findPeersOnTasks(Model model, String firstName, String secondName, String thirdName);

    QueryResultEntity recurFindTaskParents(Model model);

    QueryResultEntity findBestDays(Model model, Integer num);

    QueryResultEntity findBestPeerOnXp(Model model);

    QueryResultEntity peerTimeTracking(Model model, Integer numOfVisits, LocalTime time);

    QueryResultEntity findPeersExitNDaysMTimes(Model model, Integer nDays, Integer nTimes);

    QueryResultEntity findEarlyEntries(Model model);

    void importData(MultipartFile multipartFile, String tableName, String pathToFile);

    void exportData(String tableName, String pathToSave);

}
