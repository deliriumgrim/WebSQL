package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.VerterController;
import edu.school21.info21.web.models.data.VerterEntity;
import edu.school21.info21.web.services.data.VerterService;
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
public class VerterControllerImpl implements VerterController {

    private final VerterService verterService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public VerterControllerImpl(VerterService verterService,
                                ProcedureService procedureService,
                                @Value("${import.path}") String importPath,
                                @Value("${export.path}") String exportPath,
                                @Value("${export.filename}") String exportFilename) {
        this.verterService = verterService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/verter")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<VerterEntity> verters = this.verterService.findAll();
            model.addAttribute("verterList", verters);
            log.info("Пользователь успешно получил главную страницу таблицы Verter");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Verter. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/verter";
    }

    @Override
    @PostMapping("/addVerter")
    public String add(VerterEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.verterService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Verter");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Verter. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:verter";
    }

    @Override
    @GetMapping("findVerter")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<VerterEntity> byId = this.verterService.findById(id);
            model.addAttribute("verterList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице Verter");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Verter. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/verter";
    }

    @Override
    @PostMapping("/updateVerter")
    public String update(VerterEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.verterService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Verter");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Verter. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:verter";
    }

    @Override
    @PostMapping("deleteVerter")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.verterService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Verter");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Verter. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:verter";
    }

    @Override
    @PostMapping("/importVerter")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу P2P");
            return "redirect:/error";
        }
        return "redirect:verter";
    }

    @Override
    @GetMapping("/exportVerter")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Verter");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Verter");
            return "redirect:/error";
        }
        return null;
    }
}
