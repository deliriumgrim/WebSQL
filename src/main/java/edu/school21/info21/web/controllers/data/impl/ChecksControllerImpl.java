package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.ChecksController;
import edu.school21.info21.web.models.data.CheckEntity;
import edu.school21.info21.web.services.data.CheckService;
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
public class ChecksControllerImpl implements ChecksController {

    private final CheckService checkService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public ChecksControllerImpl(CheckService checkService,
                                ProcedureService procedureService,
                                @Value("${import.path}") String importPath,
                                @Value("${export.path}") String exportPath,
                                @Value("${export.filename}") String exportFilename) {
        this.checkService = checkService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/checks")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<CheckEntity> checks = this.checkService.findAll();
            model.addAttribute("checkList", checks);
            log.info("Пользователь успешно получил главную страницу таблицы Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу Checks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/checks";
    }

    @Override
    @PostMapping("/addCheck")
    public String add(CheckEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.checkService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Checks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:checks";
    }

    @Override
    @GetMapping("/findCheck")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<CheckEntity> checks = this.checkService.findById(id);
            model.addAttribute("checkList", checks);
            log.info("Пользователь успешно нашел запись по ID в таблице Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Checks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/checks";
    }

    @Override
    @PostMapping("/updateCheck")
    public String update(CheckEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.checkService.update(entity);
            log.warn("Пользователь успешно обновил запись в таблице Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Checks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:checks";
    }

    @Override
    @PostMapping("/deleteCheck")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.checkService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Checks. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:checks";
    }

    @Override
    @PostMapping("/importChecks")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Checks");
            return "redirect:/error";
        }
        return "redirect:checks";
    }

    @Override
    @GetMapping("/exportChecks")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Checks");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Checks: ", e);
            return "redirect:/error";
        }
        return null;
    }
}
