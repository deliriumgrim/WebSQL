package edu.school21.info21.web.controllers.operations;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;

public interface OperationsController {

    String allOperations();

    String insertP2pCheck(String checkedName, String checkingName, String taskTitle, String checkStatus, LocalTime time, Model model, RedirectAttributes redirectAttributes);

    String insertVerter(String checkedName, String taskTitle, String checkStatus, LocalTime time, Model model, RedirectAttributes redirectAttributes);

    String changePeerPoints(Model model, RedirectAttributes redirectAttributes);

    String changePeerPointsOnAnotherFunc(Model model, RedirectAttributes redirectAttributes);

    String mostChecksOnDay(Model model, RedirectAttributes redirectAttributes);

    String blockSuccess(String blockName, Model model, RedirectAttributes redirectAttributes);

    String findBestPeer(Model model, RedirectAttributes redirectAttributes);

    String statOfPeers(String firstBlock, String secondBlock, Model model, RedirectAttributes redirectAttributes);

    String checksBirthDay(Model model, RedirectAttributes redirectAttributes);

    String findPeersOnTasks(String firstName, String secondName, String thirdName, Model model, RedirectAttributes redirectAttributes);

    String recurFindTaskParents(Model model, RedirectAttributes redirectAttributes);

    String findBestDays(Integer num, Model model, RedirectAttributes redirectAttributes);

    String findBestPeerOnXp(Model model, RedirectAttributes redirectAttributes);

    String peerTimeTrack(LocalTime time, Integer numOfVisits, Model model, RedirectAttributes redirectAttributes);

    String findPeersExitNDaysMTimes(Integer nDays, Integer mTimes, Model model, RedirectAttributes redirectAttributes);

    String findEarlyEntries(Model model, RedirectAttributes redirectAttributes);

    String transferredPointReadableView(Model model, RedirectAttributes redirectAttributes);

    String successTasks(Model model, RedirectAttributes redirectAttributes);

    String notGoOutPeers(String date, Model model, RedirectAttributes redirectAttributes);
}
