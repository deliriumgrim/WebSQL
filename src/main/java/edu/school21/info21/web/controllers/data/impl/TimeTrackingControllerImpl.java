package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.TimeTrackingController;
import edu.school21.info21.web.models.data.TimeTrackingEntity;
import edu.school21.info21.web.services.data.TimeTrackingService;
import edu.school21.info21.web.services.operations.ProcedureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/data")
public class TimeTrackingControllerImpl implements TimeTrackingController {

    private final TimeTrackingService timeTrackingService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public TimeTrackingControllerImpl(TimeTrackingService timeTrackingService,
                                      ProcedureService procedureService,
                                      @Value("${import.path}") String importPath,
                                      @Value("${export.path}") String exportPath,
                                      @Value("${export.filename}") String exportFilename) {
        this.timeTrackingService = timeTrackingService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/timeTracking")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<TimeTrackingEntity> list = this.timeTrackingService.findAll();
            model.addAttribute("timeTrackingList", list);
            log.info("Пользователь успешно получил главную страницу таблицы Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Time_Tracking. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/time_tracking";
    }

    @Override
    @PostMapping("/addTimeTracking")
    public String add(TimeTrackingEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.timeTrackingService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Time_Tracking. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:timeTracking";
    }

    @Override
    @GetMapping("/findTimeTracking")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<TimeTrackingEntity> byId = this.timeTrackingService.findById(id);
            model.addAttribute("timeTrackingList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Time_Tracking. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/time_tracking";
    }

    @Override
    @PostMapping("/updateTimeTracking")
    public String update(TimeTrackingEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.timeTrackingService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Time_Tracking. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:timeTracking";
    }

    @Override
    @PostMapping("/deleteTimeTracking")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.timeTrackingService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Time_Tracking. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:timeTracking";
    }

    @Override
    @PostMapping("/importTimeTracking")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Time_Tracking");
            return "redirect:/error";
        }
        return "redirect:timeTracking";
    }

    @Override
    @GetMapping("/exportTimeTracking")
    public String exportData(@RequestParam(value = "tableName", required = false) String tableName,
                             HttpServletResponse response,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.exportData(tableName, this.exportPath);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=export.csv");
            InputStream inputStream = Files.newInputStream(Paths.get(this.exportPath));
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            log.info("Пользователь успешно экспортировал данные из таблицы Time_Tracking");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Time_Tracking");
            return "redirect:/error";
        }
        return null;
    }
}
