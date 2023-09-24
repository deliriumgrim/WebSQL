package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.TasksController;
import edu.school21.info21.web.models.data.TaskEntity;
import edu.school21.info21.web.services.data.TaskService;
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
public class TasksControllerImpl implements TasksController {

    private final TaskService taskService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public TasksControllerImpl(TaskService taskService,
                               ProcedureService procedureService,
                               @Value("${import.path}") String importPath,
                               @Value("${export.path}") String exportPath,
                               @Value("${export.filename}") String exportFilename) {
        this.taskService = taskService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/tasks")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<TaskEntity> list = this.taskService.findAll();
            model.addAttribute("taskList", list);
            log.info("Пользователь успешно получил главную страницу таблицы Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Tasks. Причина: " + e.getMessage());
            return "redirect:/error";
        }

        return "data/tables/tasks";
    }

    @Override
    @PostMapping("/addTask")
    public String add(TaskEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.taskService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Tasks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:tasks";
    }

    @Override
    @GetMapping("/findTask")
    public String findById(@RequestParam(value = "id", required = false) String id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<TaskEntity> byId = this.taskService.findById(id);
            model.addAttribute("taskList", byId);
            log.info("Пользователь успешно нашел запись по Title в таблице Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по Title в таблице Tasks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/tasks";
    }

    @Override
    @PostMapping("/updateTask")
    public String update(TaskEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.taskService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Tasks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:tasks";
    }

    @Override
    @PostMapping("/deleteTask")
    public String delete(@RequestParam(value = "id", required = false) String id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.taskService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Recommendations. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:tasks";
    }

    @Override
    @PostMapping("importTasks")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Tasks");
            return "redirect:/error";
        }
        return "redirect:tasks";
    }

    @Override
    @GetMapping("/exportTasks")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Tasks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Tasks");
            return "redirect:/error";
        }
        return null;
    }
}
