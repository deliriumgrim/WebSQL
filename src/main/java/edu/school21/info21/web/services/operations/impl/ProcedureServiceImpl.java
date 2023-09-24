package edu.school21.info21.web.services.operations.impl;

import edu.school21.info21.web.exceptions.BadInputException;
import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.repositories.operations.*;
import edu.school21.info21.web.services.operations.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalTime;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    ProcedureRepository procedureRepository;

    @Autowired
    public ProcedureServiceImpl(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Transactional
    @Override
    public void insertP2pCheck(String checkedName,
                               String checkingName,
                               String taskTitle,
                               String checkStatus,
                               LocalTime time) {
        if (taskTitle.isEmpty() ||
                checkingName.isEmpty() ||
                checkedName.isEmpty() ||
                checkStatus.isEmpty() ||
                time == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.procedureRepository.insertP2pCheck(checkedName,
                checkingName,
                taskTitle,
                checkStatus,
                time);
    }

    @Transactional
    @Override
    public void insertVerter(String checkedName,
                             String taskTitle,
                             String checkStatus,
                             LocalTime time) {
        if (taskTitle.isEmpty() ||
                checkedName.isEmpty() ||
                time == null ||
                checkStatus.isEmpty()) {

            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        this.procedureRepository.insertVerterCheck(checkedName,
                taskTitle,
                checkStatus,
                time);
    }

    @Transactional
    @Override
    public QueryResultEntity peerPointsChanges(Model model) {
        return this.procedureRepository.callPeerPointsChange();
    }

    @Transactional
    @Override
    public QueryResultEntity changePeerPointsOnAnotherFunc(Model model) {
        return this.procedureRepository.callChangePeerPointsOnAnotherFunc();
    }

    @Transactional
    @Override
    public QueryResultEntity mostChecksOnDay(Model model) {
        return this.procedureRepository.callMostChecksOnDay();
    }

    @Transactional
    @Override
    public QueryResultEntity blockSuccess(Model model,
                                          String blockName) {
        if (blockName.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.procedureRepository.callBlockSuccess(blockName);
    }

    @Transactional
    @Override
    public QueryResultEntity findBestPeer(Model model) {
        return this.procedureRepository.callFindBestPeer();
    }

    @Transactional
    @Override
    public QueryResultEntity statOfPeers(Model model,
                                         String firstBlock,
                                         String secondBlock) {
        if (firstBlock.isEmpty() || secondBlock.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.procedureRepository.callStatOfPeers(firstBlock, secondBlock);
    }

    @Transactional
    @Override
    public QueryResultEntity checksBirthday(Model model) {
        return this.procedureRepository.callChecksBirthdayEntity();
    }

    @Transactional
    @Override
    public QueryResultEntity findPeersOnTasks(Model model,
                                              String firstName,
                                              String secondName,
                                              String thirdName) {
        if (firstName.isEmpty() || secondName.isEmpty() || thirdName.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.procedureRepository.callFindPeersOnTasks(firstName, secondName, thirdName);
    }

    @Transactional
    @Override
    public QueryResultEntity recurFindTaskParents(Model model) {
        return this.procedureRepository.callRecurFindTaskParents();
    }

    @Transactional
    @Override
    public QueryResultEntity findBestDays(Model model,
                                          Integer num) {
        if (num == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.procedureRepository.callFindBestDays(num);
    }

    @Transactional
    @Override
    public QueryResultEntity findBestPeerOnXp(Model model) {
        return this.procedureRepository.callFindBestPeerOnXp();
    }

    @Transactional
    @Override
    public QueryResultEntity peerTimeTracking(Model model,
                                              Integer numOfVisits,
                                              LocalTime time) {
        if (numOfVisits == null || time == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.procedureRepository.callPeerTimeTrackingRepository(numOfVisits, time);
    }

    @Transactional
    @Override
    public QueryResultEntity findPeersExitNDaysMTimes(Model model,
                                                      Integer nDays,
                                                      Integer nTimes) {
        if (nDays == null || nTimes == null) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        return this.procedureRepository.callFindPeersExitNDaysMTimes(nDays, nTimes);
    }

    @Transactional
    @Override
    public QueryResultEntity findEarlyEntries(Model model) {
        return this.procedureRepository.callFindEarlyEntries();
    }

    @Transactional
    @Override
    public void importData(MultipartFile multipartFile,
                           String tableName,
                           String pathToFile) {
        if (multipartFile.isEmpty()
                || tableName.isEmpty()) {
            throw new BadInputException("Поля ввода не могут быть пустыми");
        }
        try {
            multipartFile.transferTo(new File(pathToFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.procedureRepository.importData(tableName, pathToFile);
    }

    @Transactional
    @Override
    public void exportData(String tableName,
                           String pathToSave) {
        this.procedureRepository.exportData(tableName, pathToSave);
    }
}
