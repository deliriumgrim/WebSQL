package edu.school21.info21.web.controllers.operations.impl;

import edu.school21.info21.web.controllers.operations.OperationsController;
import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.services.operations.FunctionService;
import edu.school21.info21.web.services.operations.ProcedureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;

@Slf4j
@Controller
@RequestMapping("/operations")
public class OperationsControllerImpl implements OperationsController {

    private final ProcedureService procedureService;
    private final FunctionService functionService;

    @Autowired
    public OperationsControllerImpl(ProcedureService insertP2pCheckService,
                                    FunctionService functionService) {
        this.procedureService = insertP2pCheckService;
        this.functionService = functionService;
    }

    @Override
    @GetMapping("/allOperations")
    public String allOperations() {
        log.info("Пользователь получил главную страницу со списком операций");
        return "operations/sql_queries";
    }

    @Override
    @PostMapping("/insertP2pCheck")
    public String insertP2pCheck(@RequestParam(value = "checkedName", required = false) String checkedName,
                                 @RequestParam(value = "checkingName", required = false) String checkingName,
                                 @RequestParam(value = "taskTitle", required = false) String taskTitle,
                                 @RequestParam(value = "checkStatus", required = false) String checkStatus,
                                 @RequestParam(value = "time", required = false) LocalTime time,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.insertP2pCheck(checkedName,
                    checkingName,
                    taskTitle,
                    checkStatus,
                    time);
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 1");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 1. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @PostMapping("/insertVerter")
    public String insertVerter(@RequestParam(value = "checkedName", required = false) String checkedName,
                               @RequestParam(value = "taskTitle", required = false) String taskTitle,
                               @RequestParam(value = "checkStatus", required = false) String checkStatus,
                               @RequestParam(value = "time", required = false) LocalTime time,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.insertVerter(checkedName,
                    taskTitle,
                    checkStatus,
                    time);
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 2");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 2. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/changePeerPoints")
    public String changePeerPoints(Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.peerPointsChanges(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 6");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 6. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/changePeerPointsOnAnotherFunc")
    public String changePeerPointsOnAnotherFunc(Model model,
                                                RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.changePeerPointsOnAnotherFunc(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 7");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 7. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/mostChecksOnDay")
    public String mostChecksOnDay(Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.mostChecksOnDay(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 8");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 8. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/blockSuccess")
    public String blockSuccess(@RequestParam(value = "blockName", required = false) String blockName,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.blockSuccess(model, blockName);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 9");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 9. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/findBestPeer")
    public String findBestPeer(Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.findBestPeer(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 10");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 10. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/statOfPeers")
    public String statOfPeers(@RequestParam(value = "firstBlock", required = false) String firstBlock,
                              @RequestParam(value = "secondBlock", required = false) String secondBlock,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.statOfPeers(model, firstBlock, secondBlock);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 11");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 11. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/checksBirthday")
    public String checksBirthDay(Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.checksBirthday(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 12");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 12. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/findPeersOnTasks")
    public String findPeersOnTasks(@RequestParam(value = "firstName", required = false) String firstName,
                                   @RequestParam(value = "secondName", required = false) String secondName,
                                   @RequestParam(value = "thirdName", required = false) String thirdName,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.findPeersOnTasks(model, firstName, secondName, thirdName);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 13");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 13. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/recurFindTaskParents")
    public String recurFindTaskParents(Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.recurFindTaskParents(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 14");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 14. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/findBestDays")
    public String findBestDays(@RequestParam(value = "countOfDays", required = false) Integer num,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.findBestDays(model, num);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 15");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 15. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/findBestPeerOnXp")
    public String findBestPeerOnXp(Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.findBestPeerOnXp(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 16");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 16. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/peerTimeTrack")
    public String peerTimeTrack(@RequestParam(value = "time", required = false) LocalTime time,
                                @RequestParam(value = "numOfVisits", required = false) Integer numOfVisits,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.peerTimeTracking(model, numOfVisits, time);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 17");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 17. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/findPeersExitNDaysMTimes")
    public String findPeersExitNDaysMTimes(@RequestParam(value = "nDays", required = false) Integer nDays,
                                           @RequestParam(value = "nTimes", required = false) Integer nTimes,
                                           Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.findPeersExitNDaysMTimes(model, nDays, nTimes);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 18");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 18. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/findEarlyEntries")
    public String findEarlyEntries(Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.procedureService.findEarlyEntries(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 19");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 19. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/transferredPointsReadableView")
    public String transferredPointReadableView(Model model,
                                               RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.functionService.transferredPointsReadableView(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 20");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 20. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/successTasks")
    public String successTasks(Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.functionService.successTasks(model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 21");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 21. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }

    @Override
    @GetMapping("/notGoOutPeers")
    public String notGoOutPeers(@RequestParam(value = "date", required = false) String date,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult
                    = this.functionService.notGoOutPeers(date, model);
            model.addAttribute("columns", queryResult.getColumns());
            model.addAttribute("rows", queryResult.getRows());
            model.addAttribute("backPage", "allOperations");
            log.info("Пользователь успешно выполнил процедуру 22");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог выполнить процедуру 22. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }
}
